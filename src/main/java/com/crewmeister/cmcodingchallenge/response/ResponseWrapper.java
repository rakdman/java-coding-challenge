package com.crewmeister.cmcodingchallenge.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ResponseWrapper<T> {
    private T data;
    private Meta meta;
}
