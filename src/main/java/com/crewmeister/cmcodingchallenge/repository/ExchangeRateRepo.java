package com.crewmeister.cmcodingchallenge.repository;

import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface ExchangeRateRepo extends JpaRepository<ExchangeRate, Long> {

    @Query(value = "select distinct(currencyUnit) from ExchangeRate")
    List<String> findDistinctCurrencyUnit();

    List<ExchangeRate> findByDate(Date date);

    ExchangeRate findByDateAndCurrencyUnit(Date date, String currencyUnit);

}
