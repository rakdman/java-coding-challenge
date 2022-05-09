package com.crewmeister.cmcodingchallenge.util;

import com.crewmeister.cmcodingchallenge.request.RequestAttributeEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides many convenience methods.
 */
public class HelperUtil {

    /**
     * The method does the date validation check based on expected format.
     *
     * @return boolean
     * @params String for date, String for format
     */
    public static boolean isValidDateFormat(String inputDate, String expectedFormat) {

        if (inputDate != null) {
            try {
                LocalDate outputDate = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern(expectedFormat));
                if (outputDate.isAfter(LocalDate.now()))
                    return false;

            } catch (DateTimeParseException e) {
                return false;
            }

        }
        return true;
    }

    /**
     * The method convert date value in string format to date.
     *
     * @return Date
     * @params String
     */
    public static Date convertStringToDate(String inputDate) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(inputDate);
        } catch (ParseException e) {
            e.getMessage();
        }
        return date;
    }


    /**
     * The method does validation check of input request object.
     *
     * @return Map of status and message
     * @params RequestAttributeEntity
     */
    public static Map<String, ?> isValidRequestEntity(RequestAttributeEntity requestAttributeEntity, List<String> currencyUnits) {

        Map<String, Object> validationResponse = new HashMap<>();

        //Check if none of the three parameters are null
        if (!(requestAttributeEntity != null && requestAttributeEntity.getDate() != null && requestAttributeEntity.getInputAmount() != null && requestAttributeEntity.getCurrencyUnit() != null)) {
            validationResponse.put("status", Boolean.FALSE);
            validationResponse.put("message", new String("One the input parameters is missing!"));
            return validationResponse;
        }

        //Date validation
        if (!HelperUtil.isValidDateFormat(requestAttributeEntity.getDate(), "yyyy-MM-dd")) {
            validationResponse.put("status", Boolean.FALSE);
            validationResponse.put("message", new String("Input date is not valid!"));
            return validationResponse;
        }

        //Currency validation check
        if (!currencyUnits.contains(requestAttributeEntity.getCurrencyUnit())) {
            validationResponse.put("status", Boolean.FALSE);
            validationResponse.put("message", new String("Currency unit is not valid!"));
            return validationResponse;
        }

        //Input amount validation check
        if (requestAttributeEntity.getInputAmount() <= 0) {
            validationResponse.put("status", Boolean.FALSE);
            validationResponse.put("message", new String("Currency amount is not valid!"));
            return validationResponse;
        }

        validationResponse.put("status", Boolean.TRUE);
        validationResponse.put("message", new String("Validation successful!"));

        return validationResponse;

    }
}
