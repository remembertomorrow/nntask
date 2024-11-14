package com.example.nntask.model.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountRequest {

    private String firstName;

    private String lastName;

    @DecimalMin(value = "0.01", message = "Initial balance must be positive")
    @Digits(integer = 10, fraction = 2, message = "Initial balance cannot have more than 10 digits and 2 decimal places")
    private BigDecimal initialBalance;

}
