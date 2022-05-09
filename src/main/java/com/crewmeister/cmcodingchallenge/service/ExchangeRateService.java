package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import com.crewmeister.cmcodingchallenge.repository.ExchangeRateRepo;
import com.crewmeister.cmcodingchallenge.request.RequestAttributeEntity;
import com.crewmeister.cmcodingchallenge.response.Meta;
import com.crewmeister.cmcodingchallenge.response.ResponseWrapper;
import com.crewmeister.cmcodingchallenge.util.HelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Service class for currencyController.
 */
@Service
public class ExchangeRateService {

    @Autowired
    ExchangeRateRepo exchangeRateRepo;


    /**
     * The method returns the list of currencies.
     *
     * @return List of currency unit.
     * @params
     */
    public ResponseWrapper<?> getAllCurrencyUnits() {
        List<String> currencyUnits = exchangeRateRepo.findDistinctCurrencyUnit();

        if (currencyUnits.size() == 0)
            return new ResponseWrapper<>(currencyUnits, new Meta(HttpStatus.NO_CONTENT, "No currencies available!"));
        else
            return new ResponseWrapper<>(currencyUnits, new Meta(HttpStatus.OK, "List of available currencies!"));

    }


    /**
     * The method returns the list of all exchange rates.
     *
     * @return List of all exchange rates.
     * @params
     */
    public ResponseWrapper<?> getAllExchangeRates() {

        List<ExchangeRate> exchangeRates = exchangeRateRepo.findAll();

        if (exchangeRates.size() != 0)
            return new ResponseWrapper<>(exchangeRates, new Meta(HttpStatus.OK, "List of exchange rates!"));
        else
            return new ResponseWrapper<>(exchangeRates, new Meta(HttpStatus.NO_CONTENT, "No exchange rate exists!"));

    }


    /**
     * The method returns the list of all exchange rates based on date.
     *
     * @return List of exchange rates.
     * @params Date
     */
    public ResponseWrapper<?> getExchangeRateOfDate(String inputDate) {

        if (inputDate != null) {

            if (!HelperUtil.isValidDateFormat(inputDate, "yyyy-MM-dd")) {
                return new ResponseWrapper<String>("Invalid input date!", new Meta(HttpStatus.BAD_REQUEST, "Expected input date format is yyyy-MM-dd and can be until today."));
            }

            Date date = HelperUtil.convertStringToDate(inputDate);
            List<ExchangeRate> exchangeRates = exchangeRateRepo.findByDate(date);

            if (exchangeRates.size() == 0)
                return new ResponseWrapper<String>("No exchange rate found for the given date.", new Meta(HttpStatus.NO_CONTENT, "No data"));
            else {
                return new ResponseWrapper<>(exchangeRates, new Meta(HttpStatus.OK, "List of exchange rates."));
            }
        } else
            return new ResponseWrapper<String>("No conversion rate found for the given date", new Meta(HttpStatus.NO_CONTENT, "No data"));
    }


    /**
     * The method returns the list of all exchange rates based on date and currency unit code.
     *
     * @return List of exchange rates.
     * @params inputDate, currencyUnit code
     */
    public ResponseWrapper<?> getExchangeRateOfDateAndCurrencyUnit(String inputDate, String currencyUnit) {

        List<String> currencyUnits = exchangeRateRepo.findDistinctCurrencyUnit();

        if (inputDate != null) {
            if (!(HelperUtil.isValidDateFormat(inputDate, "yyyy-MM-dd")) && !(currencyUnits.contains(currencyUnit))) {
                return new ResponseWrapper<String>("Invalid input date!", new Meta(HttpStatus.BAD_REQUEST, "Expected input date format is yyyy-MM-dd and to be until today"));
            }

            Date date = HelperUtil.convertStringToDate(inputDate);
            ExchangeRate exchangeRate = exchangeRateRepo.findByDateAndCurrencyUnit(date, currencyUnit);

            if (exchangeRate == null)
                return new ResponseWrapper<String>("No conversion rate found for the given date, currency", new Meta(HttpStatus.NO_CONTENT, "No data"));
            else {
                return new ResponseWrapper<>(exchangeRate.getConversionRate(), new Meta(HttpStatus.OK, "Exchange rate."));
            }
        }
        return new ResponseWrapper<String>("No conversion rate found for the given date", new Meta(HttpStatus.NO_CONTENT, "No data"));
    }


    /**
     * The method returns the list of all exchange rates based on date and currency unit code.
     *
     * @return List of exchange rates.
     * @params inputDate, currencyUnit code
     */
    public ResponseWrapper<?> getExchangeAmount(RequestAttributeEntity requestAttributeEntity) throws ParseException {

        List<String> currencyUnits = exchangeRateRepo.findDistinctCurrencyUnit();

        //Does validation check for all three input fields
        Boolean reqValidationChkStatus = (Boolean) HelperUtil.isValidRequestEntity(requestAttributeEntity, currencyUnits).get("status");

        //Get the validation message
        String reqValidationChkMessage = (String) HelperUtil.isValidRequestEntity(requestAttributeEntity, currencyUnits).get("message");

        if (reqValidationChkStatus) {
//            Get conversion rate and calculate the equivalent EUR amount
            Double exchangeRate = exchangeRateRepo.findByDateAndCurrencyUnit(HelperUtil.convertStringToDate(requestAttributeEntity.getDate()), requestAttributeEntity.getCurrencyUnit()).getConversionRate();
            DecimalFormat decimalFormat = new DecimalFormat("#.####");
            Double convertedAmount = Double.parseDouble(decimalFormat.format(requestAttributeEntity.getInputAmount() / exchangeRate));
            return new ResponseWrapper<Double>(convertedAmount, new Meta(HttpStatus.OK, "Converted amount"));
        } else
            return new ResponseWrapper<Double>(null, new Meta(HttpStatus.BAD_REQUEST, reqValidationChkMessage));
    }
}
