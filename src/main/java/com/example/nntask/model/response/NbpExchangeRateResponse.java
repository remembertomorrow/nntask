package com.example.nntask.model.response;

import com.example.nntask.model.Rate;
import lombok.Data;

import java.util.List;

@Data
public class NbpExchangeRateResponse {
    private String code;
    private List<Rate> rates;
}

