package com.example.nntask.fixture;

import com.example.nntask.model.Rate;
import com.example.nntask.model.request.ConvertCurrencyRequest;
import com.example.nntask.model.response.NbpExchangeRateResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FixtureBuilder {

    public static ConvertCurrencyRequest buildConvertCurrencyRequest(String origin, String target, UUID accountId) {
        ConvertCurrencyRequest request = new ConvertCurrencyRequest();
        request.setAccountId(accountId);
        request.setOriginCurrency(origin);
        request.setTargetCurrency(target);
        request.setOriginAmount(BigDecimal.valueOf(100));
        return request;
    }

    public static NbpExchangeRateResponse buildNbpExchangeRateResponse() {
        NbpExchangeRateResponse nbpRate = new NbpExchangeRateResponse();
        nbpRate.setCode("USD");
        List<Rate> rates = new ArrayList<>();
        Rate rate = new Rate();
        rate.setAsk(5.00);
        rate.setBid(2.50);
        rates.add(rate);
        nbpRate.setRates(rates);
        return nbpRate;
    }

}