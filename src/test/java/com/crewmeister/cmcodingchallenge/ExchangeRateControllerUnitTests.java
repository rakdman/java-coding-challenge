package com.crewmeister.cmcodingchallenge;

import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import com.crewmeister.cmcodingchallenge.request.RequestAttributeEntity;
import com.crewmeister.cmcodingchallenge.response.Meta;
import com.crewmeister.cmcodingchallenge.response.ResponseWrapper;
import com.crewmeister.cmcodingchallenge.service.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test cases for all methods of Controller by mocking service methods.
 */
@SpringBootTest
public class ExchangeRateControllerUnitTests {

    @MockBean
    ExchangeRateService exchangeRateService;

    /**
     * It tests if it returns list of currencies
     *
     * @return
     * @params
     */
    @Test
    public void getCurrenciesTest() {
        ResponseWrapper responseWrapper = new ResponseWrapper(List.of("AUD", "BRL"), new Meta(HttpStatus.OK, "Ok"));
        Mockito.when(exchangeRateService.getAllCurrencyUnits()).thenReturn(responseWrapper);
        assertEquals(List.of("AUD", "BRL"), exchangeRateService.getAllCurrencyUnits().getData());
    }

    /**
     * It tests if it returns exchange rates based on input date
     *
     * @return collection of exchange rates
     * @params date
     */
    @Test
    public void getExchangeRateOfDateTest() {
        ExchangeRate exchangeRate = new ExchangeRate(new Date(), 1.22, "AUD");
        Meta metaData = new Meta(HttpStatus.OK, "Ok");
        ResponseWrapper responseWrapper = new ResponseWrapper(exchangeRate, metaData);
        Mockito.when(exchangeRateService.getExchangeRateOfDate("2022-05-06")).thenReturn(responseWrapper);
        assertEquals(exchangeRate, exchangeRateService.getExchangeRateOfDate("2022-05-06").getData());
    }

    /**
     * It tests if it returns exchange rates based on input date and currencyUnit
     *
     * @return collection of exchange rates
     * @params date, currencyUnit
     */
    @Test
    public void getExchangeRateOfDateAndCurrencyUnitTest() {
        ExchangeRate exchangeRate = new ExchangeRate(new Date(), 1.22, "AUD");
        Meta metaData = new Meta(HttpStatus.OK, "Ok");
        ResponseWrapper responseWrapper = new ResponseWrapper(exchangeRate, metaData);
        Mockito.when(exchangeRateService.getExchangeRateOfDateAndCurrencyUnit("2022-05-06", "AUD")).thenReturn(responseWrapper);
        assertEquals(exchangeRate, exchangeRateService.getExchangeRateOfDateAndCurrencyUnit("2022-05-06", "AUD").getData());
    }

    /**
     * It tests conversion based on input date, currencyUnit and inputAmount
     *
     * @return converted amount
     * @params date, currencyUnit, inputAmount
     */
    @Test
    public void getExchangeAmountTest() throws ParseException {

        RequestAttributeEntity requestAttributeEntity = new RequestAttributeEntity("2022-05-05", "AUD", 2000.00);

        Meta metaData = new Meta(HttpStatus.OK, "Ok");
        ResponseWrapper responseWrapper = new ResponseWrapper(1363.4195, metaData);

        Mockito.when(exchangeRateService.getExchangeAmount(requestAttributeEntity)).thenReturn(responseWrapper);
        assertEquals(1363.4195, exchangeRateService.getExchangeAmount(requestAttributeEntity).getData());

    }

}
