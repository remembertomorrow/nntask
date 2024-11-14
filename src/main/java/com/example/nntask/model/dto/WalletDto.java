package com.example.nntask.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Data
public class WalletDto {

    private UUID walletId;

    private UUID accountId;

    private String walletCurrency;

    private BigDecimal amount;

}
