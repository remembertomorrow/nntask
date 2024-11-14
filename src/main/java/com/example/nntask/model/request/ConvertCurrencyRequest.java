package com.example.nntask.model.request;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Getter
public class ConvertCurrencyRequest {

    private UUID accountId;

    private BigDecimal originAmount;

    private String originCurrency;

    private String targetCurrency;

    @PostConstruct
    public void init() {
        if (originAmount != null) {
            originAmount = originAmount.setScale(4, RoundingMode.HALF_EVEN);
        }
    }

}
