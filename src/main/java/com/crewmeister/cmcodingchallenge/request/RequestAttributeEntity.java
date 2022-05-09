package com.crewmeister.cmcodingchallenge.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RequestAttributeEntity {
    private String date;
    private String currencyUnit;
    private Double inputAmount;
}
