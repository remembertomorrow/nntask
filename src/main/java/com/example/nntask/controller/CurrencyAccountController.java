package com.example.nntask.controller;

import com.example.nntask.model.request.CreateCurrencyAccountRequest;
import com.example.nntask.model.response.CreateCurrencyAccountResponse;
import com.example.nntask.model.response.GetCurrencyAccountResponse;
import com.example.nntask.service.CurrencyAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/currency-accounts")
public class CurrencyAccountController {


    private final CurrencyAccountService currencyAccountService;


    @GetMapping("/{id}")
    public GetCurrencyAccountResponse getCurrencyAccount(@PathVariable UUID id) {
        return currencyAccountService.getCurrencyAccountById(id);
    }

    @PostMapping
    public CreateCurrencyAccountResponse createCurrencyAccount(
            @RequestBody CreateCurrencyAccountRequest request
    ) {
        log.info("Initiating request to create currency account, req: {}", request);
        return currencyAccountService.createCurrencyAccount(request);
    }

}
