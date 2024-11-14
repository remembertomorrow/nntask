package com.example.nntask.model.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountRequest {

    private String firstName;

    private String lastName;

    private BigDecimal initialBalance;

}
