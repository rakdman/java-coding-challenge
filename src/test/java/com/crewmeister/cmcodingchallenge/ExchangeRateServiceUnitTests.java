package com.crewmeister.cmcodingchallenge;

import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import com.crewmeister.cmcodingchallenge.repository.ExchangeRateRepo;
import com.crewmeister.cmcodingchallenge.request.RequestAttributeEntity;
import com.crewmeister.cmcodingchallenge.service.ExchangeRateService;
import com.crewmeister.cmcodingchallenge.util.HelperUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Unit test cases for all methods of ExchangeRateService class by mocking repository methods.
 */

@SpringBootTest
public class ExchangeRateServiceUnitTests {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @MockBean
    private ExchangeRateRepo exchangeRateRepo;

    /**
     * The method test if it returns all currencies as list
     *
     * @return
     * @params
     */
    @Test
    public void getAllCurrencyUnitsTest() {
        Mockito.when(exchangeRateRepo.findDistinctCurrencyUnit())
                .thenReturn(List.of("AUD", "BLR"));
        assertEquals(List.of("AUD", "BLR"), exchangeRateService.getAllCurrencyUnits().getData());
    }

    /**
     * The method test if it returns all exchange rates
     *
     * @return
     * @params
     */
    @Test
    public void getExchangeRatesTest() {
        List<ExchangeRate> responseData = List.of(new ExchangeRate(new Date(), 0.0, "AUD"),
                new ExchangeRate(new Date(), 0.0, "AUD"));
        Mockito.when(exchangeRateRepo.findAll()).thenReturn(responseData);
        assertEquals(HttpStatus.OK, exchangeRateService.getAllExchangeRates().getMeta().getHttpStatusCode());
    }


    /**
     * The method test if it returns all currency exchange rates based on date
     *
     * @return
     * @params date
     */
    @Test
    public void getExchangeRateOfDateTest() {
        List<ExchangeRate> responseData = List.of(new ExchangeRate
                (HelperUtil.convertStringToDate("2022-05-06"), 1.4888, "AUD"));
        Mockito.when(exchangeRateRepo.findByDate(HelperUtil.convertStringToDate("2022-05-06"))).thenReturn(responseData);
        assertEquals(HttpStatus.OK, exchangeRateService.getExchangeRateOfDate("2022-05-06").getMeta().getHttpStatusCode());
    }

    /**
     * The method test if it returns currency exchange rate based on date and currencyUnit
     *
     * @return
     * @params date, currencyUnit
     */
    @Test
    public void getExchangeRateOfDateAndCurrencyUnitTest() {
        String inputDate = "2022-05-06";
        String currencyUnit = "AUD";
        ExchangeRate responseData = new ExchangeRate
                (HelperUtil.convertStringToDate("2022-05-06"), 1.4888, "AUD");
        Date date = HelperUtil.convertStringToDate(inputDate);
        Mockito.when(exchangeRateRepo.findByDateAndCurrencyUnit(date, currencyUnit)).thenReturn(responseData);
        assertEquals(HttpStatus.OK, exchangeRateService.getExchangeRateOfDateAndCurrencyUnit("2022-05-06", "AUD").getMeta().getHttpStatusCode());
    }


    /**
     * The method test the conversion amount
     *
     * @return date, currencyUnit, inputAmount
     * @params converted amount
     */
    @Test
    public void getExchangeAmountTest() throws ParseException {
        RequestAttributeEntity requestAttributeEntity = new RequestAttributeEntity("2022-05-05", "AUD", 2000.00);
        Date date = HelperUtil.convertStringToDate(requestAttributeEntity.getDate());
        ExchangeRate exchangeRate = new ExchangeRate(HelperUtil.convertStringToDate("2022-05-05"), 1.4888, "AUD");

        Mockito.when(exchangeRateRepo.findByDateAndCurrencyUnit(date, "AUD")).thenReturn(exchangeRate);

        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        Double convertedAmount = Double.parseDouble(decimalFormat.format(requestAttributeEntity.getInputAmount() / exchangeRate.getConversionRate()));
        assertEquals(1343.3638, convertedAmount);

    }
}
