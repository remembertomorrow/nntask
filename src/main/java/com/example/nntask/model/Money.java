package com.example.nntask.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;

@Data
public class Money {

    private BigDecimal amount;

    private Currency currencyCode;

}
