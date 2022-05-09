package com.crewmeister.cmcodingchallenge.entity;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Validated
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @NotNull
    @Temporal(TemporalType.DATE)
    Date date;

    @NotNull
    double conversionRate;

    @NotNull
    String currencyUnit;

    public ExchangeRate(Date date, Double conversionRate, String currencyUnit) {
        this.date = date;
        this.conversionRate = conversionRate;
        this.currencyUnit = currencyUnit;
    }
}
