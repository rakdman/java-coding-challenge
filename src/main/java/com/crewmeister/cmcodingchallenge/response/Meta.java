package com.crewmeister.cmcodingchallenge.response;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Meta {
    @NotNull
    private HttpStatus httpStatusCode;
    private String message;
}
