package com.crewmeister.cmcodingchallenge.util;

import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import com.crewmeister.cmcodingchallenge.service.CsvService;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


/**
 * This class loads the currency exchange rates from the Bundesbank using multi-threading into h2 db.
 */
@Component
@Log4j2
public class CsvUtil implements CommandLineRunner {

    @Autowired
    CsvService csvService;

    @Value("${number-of-threads}")
    private int numberOfThreads;
    /**
     * The method called during initialization to load the data into h2 db.
     *
     * @return
     * @params
     */
    @Override
    public void run(String... args) throws IOException {

        //Read pre-configured list of Bundesbank daily exchange rates urls from property file
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("exchangerate.properties");
        List<String> urls = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.toList());


        // Set the no of threads and total no of files
        int totalFilesToLoad;

        if (urls.size() > 0)
        {
            totalFilesToLoad = urls.size();
            int noOfThreads = numberOfThreads;

            if (totalFilesToLoad < numberOfThreads)
                noOfThreads = totalFilesToLoad;

            int filesPerThread = totalFilesToLoad / noOfThreads;
            String[][] urlDistributionArray = new String[noOfThreads][filesPerThread];

            //Distribute the exchange rate files into noOfThreads threads
            for (int i = 0; i < noOfThreads; i++) {
                for (int j = i * filesPerThread, k = 0; j < i * filesPerThread + filesPerThread; j++, k++) {
                    urlDistributionArray[i][k] = urls.get(j);
                }
            }

            ExecutorService executorService = Executors.newFixedThreadPool(noOfThreads);

            //Create runnable objects to create threads
            for (int i = 0; i < noOfThreads; i++) {
                int finalI = i;
                Runnable uploadThread$i = () -> {
                    for (int urlId = 0; urlId < filesPerThread; urlId++) {
                        readOneDataFile(urlDistributionArray[finalI][urlId]);
                    }
                };
                executorService.execute(uploadThread$i);
            }

            executorService.shutdown();
        } else
            log.error("No files to load!");

    }

    /**
     * The method to read data from the exchange rate file.
     *
     * @return
     * @params exchange rate file url
     */
    public void readOneDataFile(String url) {
        RestTemplate restTemplate = new RestTemplate();
        Resource resource;
        resource = restTemplate.getForObject(url, Resource.class);
        List<ExchangeRate> exchangeRates = null;

        //Extracting Currency Unit/Code fromt the file name
        String currencyUnit = resource.getFilename().split("\\.")[2];

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            CSVReader csvReader = new CSVReaderBuilder(br).withSkipLines(9).build();
            List<String[]> allData = csvReader.readAll();
            exchangeRates = new ArrayList<>();

            for (String[] rows : allData) {
                LocalDate date = LocalDate.parse(rows[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                Date mydate = Date.valueOf(date);
                Double rate = 0.00;

                // Initializing exchangeRate value if valid number
                if (!rows[1].trim().equals(".")) {
                    rate = Double.valueOf(rows[1]);
                }
                ExchangeRate exchangeRate = new ExchangeRate(mydate, rate, currencyUnit);
                exchangeRates.add(exchangeRate);
            }

            //call method to save the data in db
            this.uploadOneDataFile(exchangeRates);

        } catch (Exception e) {
            log.error(e.getStackTrace());
         }
    }

    /**
     * The method to load the exchange rate list to db.
     *
     * @return
     * @params exchange rate list
     */
    public void uploadOneDataFile(List<ExchangeRate> exchangeRates) {
        csvService.save(exchangeRates);
    }
}
