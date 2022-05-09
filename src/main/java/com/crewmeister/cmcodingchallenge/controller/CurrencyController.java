package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.request.RequestAttributeEntity;
import com.crewmeister.cmcodingchallenge.response.ResponseWrapper;
import com.crewmeister.cmcodingchallenge.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;

/**
 * The controller for exchange rates and amount conversion.
 */
@RestController()
@RequestMapping("/api")
public class CurrencyController {

    @Autowired
    ExchangeRateService exchangeRateService;

    /**
     * The method returns the list of currencies.
     *
     * @return The ResponseWrapper object having list of currencies.
     * @params
     */
    @GetMapping(value = "/currencies", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper<?> getCurrencies() {
        return exchangeRateService.getAllCurrencyUnits();
    }

    /**
     * The method returns the list of currencies based on none or one or more input parameters.
     *
     * @return The ResponseWrapper object having collection of exchange rates.
     * @params inputDate, currencyUnit and both are optional.
     */
    @GetMapping(value = "/exchangerate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper getExchangeRateOfDate(@RequestParam(value = "inputDate", required = false) String inputDate, @RequestParam(value = "currencyUnit", required = false) String currencyUnit) {

        ResponseWrapper exchangeRate;

        if (inputDate != null && currencyUnit != null)
            exchangeRate = exchangeRateService.getExchangeRateOfDateAndCurrencyUnit(inputDate, currencyUnit);
        else if (inputDate != null)
            exchangeRate = exchangeRateService.getExchangeRateOfDate(inputDate);
        else
            exchangeRate = exchangeRateService.getAllExchangeRates();

        return exchangeRate;
    }

    /**
     * The method returns the converted amount based date, currencyUnit and amount.
     *
     * @return The ResponseWrapper object having converted amount.
     * @params date, currencyUnit, amount and all are mandatory.
     */
    @GetMapping(value = "/currencyconversion", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseWrapper<?> getExchangeRateOfDate(@RequestBody RequestAttributeEntity requestAttributeEntity) throws ParseException {

        ResponseWrapper convertedAmount = exchangeRateService.getExchangeAmount(requestAttributeEntity);

        return convertedAmount;
    }
}
