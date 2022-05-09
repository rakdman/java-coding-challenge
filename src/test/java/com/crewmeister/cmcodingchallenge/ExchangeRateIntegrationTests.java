package com.crewmeister.cmcodingchallenge;

import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import com.crewmeister.cmcodingchallenge.response.Meta;
import com.crewmeister.cmcodingchallenge.response.ResponseWrapper;
import com.crewmeister.cmcodingchallenge.service.ExchangeRateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test cases for all api end-points by mocking service methods.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ExchangeRateIntegrationTests {

    ObjectMapper om = new ObjectMapper();

    @MockBean
    ExchangeRateService exchangeRateService;
    @Autowired
    private MockMvc mockMvc;

    /**
     * The method tests the currencies endpoint
     *
     * @return
     * @params
     */
    @Test
    void getCurrencies() throws Exception {

        ResponseWrapper responseWrapper = new ResponseWrapper(List.of("AUD", "BRL"), new Meta(HttpStatus.OK, "Ok"));
        Mockito.when(exchangeRateService.getAllCurrencyUnits()).thenReturn(responseWrapper);

        MvcResult result = mockMvc.perform(get("/api/currencies"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String resultResponse = result.getResponse().getContentAsString();
        ResponseWrapper resultResponseWrapper = om.readValue(resultResponse, ResponseWrapper.class);
        List<String> currencies = (List<String>) resultResponseWrapper.getData();
        Assert.assertEquals(2, currencies.size());

    }


    /**
     * The method tests the exchangerate endpoint without any inputs
     *
     * @return
     * @params
     */
    @Test
    public void getExchangesTest() throws Exception {
        ExchangeRate exchangeRate = new ExchangeRate(new Date(), 1.22, "AUD");
        Meta metaData = new Meta(HttpStatus.OK, "Ok");
        ResponseWrapper responseWrapper = new ResponseWrapper(exchangeRate, metaData);

        Mockito.when(exchangeRateService.getAllExchangeRates()).thenReturn(responseWrapper);

        MvcResult result = mockMvc.perform(get("/api/exchangerate"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String resultResponse = result.getResponse().getContentAsString();
        ResponseWrapper resultResponseWrapper = om.readValue(resultResponse, ResponseWrapper.class);
        Assert.assertEquals(exchangeRate, exchangeRateService.getAllExchangeRates().getData());
    }

    /**
     * The method tests the exchangerate endpoint with date as input
     *
     * @return
     * @params
     */
    @Test
    public void getExchangeRateOfDateTest() throws Exception {
        ExchangeRate exchangeRate = new ExchangeRate(new Date(), 1.22, "AUD");
        Meta metaData = new Meta(HttpStatus.OK, "Ok");
        ResponseWrapper responseWrapper = new ResponseWrapper(exchangeRate, metaData);

        Mockito.when(exchangeRateService.getExchangeRateOfDate("2022-05-06")).thenReturn(responseWrapper);

        MvcResult result = mockMvc.perform(get("/api/exchangerate").param("inputDate", "2022-05-06"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String resultResponse = result.getResponse().getContentAsString();
        ResponseWrapper resultResponseWrapper = om.readValue(resultResponse, ResponseWrapper.class);
        Assert.assertEquals(exchangeRate, exchangeRateService.getExchangeRateOfDate("2022-05-06").getData());
    }


    /**
     * The method tests the exchangerate endpoint with date and currencyUnit as input
     *
     * @return
     * @params
     */
    @Test
    public void getExchangeRateOfDateAndCurrencyUnitTest() throws Exception {
        ExchangeRate exchangeRate = new ExchangeRate(new Date(), 1.22, "AUD");
        Meta metaData = new Meta(HttpStatus.OK, "Ok");
        ResponseWrapper responseWrapper = new ResponseWrapper(exchangeRate, metaData);

        Mockito.when(exchangeRateService.getExchangeRateOfDateAndCurrencyUnit("2022-05-06", "AUD")).thenReturn(responseWrapper);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("inputDate", "2022-05-06");
        params.add("currencyUnit", "AUD");

        MvcResult result = mockMvc.perform(get("/api/exchangerate").params(params))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String resultResponse = result.getResponse().getContentAsString();
        ResponseWrapper resultResponseWrapper = om.readValue(resultResponse, ResponseWrapper.class);
        Assert.assertEquals(exchangeRate, exchangeRateService.getExchangeRateOfDateAndCurrencyUnit("2022-05-06", "AUD").getData());

    }
}
