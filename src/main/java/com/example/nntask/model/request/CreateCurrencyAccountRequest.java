package com.example.nntask.model.request;

import com.example.nntask.model.Money;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateCurrencyAccountRequest {

    private String firstName;

    private String lastName;

    private Money initialBalance;

}
