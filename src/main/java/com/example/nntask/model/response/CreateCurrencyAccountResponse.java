package com.example.nntask.model.response;

import com.example.nntask.model.Money;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateCurrencyAccountResponse {

    private UUID accountId;

    private String firstName;

    private String lastName;

    private Money initialBalance;

}
