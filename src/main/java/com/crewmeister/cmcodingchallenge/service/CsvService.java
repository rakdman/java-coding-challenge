package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import com.crewmeister.cmcodingchallenge.repository.ExchangeRateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service class for CsvUtil class.
 */
@Service
public class CsvService {

    @Autowired
    ExchangeRateRepo exchangeRateRepo;

    public void save(List<ExchangeRate> exchangeRateList) {
        exchangeRateRepo.saveAll(exchangeRateList);
    }

    public List<ExchangeRate> getAll() {
        return exchangeRateRepo.findAll();
    }

}
