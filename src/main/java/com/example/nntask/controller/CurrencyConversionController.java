package com.example.nntask.controller;

import com.example.nntask.model.request.ConvertCurrencyRequest;
import com.example.nntask.model.response.GetAccountResponse;
import com.example.nntask.service.CurrencyConversionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/conversion")
public class CurrencyConversionController {

    private final CurrencyConversionService currencyConversionService;


    @PostMapping
    public GetAccountResponse convertCurrency(
            @RequestBody ConvertCurrencyRequest request
    ) {
        log.info("Initiating request to convert currencies: {}", request);
        return currencyConversionService.convertCurrency(request);
    }

}
