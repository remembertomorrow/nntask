package com.example.nntask.controller;

import com.example.nntask.service.CurrencyAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CurrencyAccountController {

    private final CurrencyAccountService currencyAccountService;



}
