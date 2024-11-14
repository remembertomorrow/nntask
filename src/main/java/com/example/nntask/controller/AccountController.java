package com.example.nntask.controller;

import com.example.nntask.model.request.CreateAccountRequest;
import com.example.nntask.model.response.CreateAccountResponse;
import com.example.nntask.model.response.GetAccountResponse;
import com.example.nntask.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {


    private final AccountService accountService;


    @GetMapping("/{id}")
    public GetAccountResponse getAccount(@PathVariable UUID id) {
        return accountService.getAccountById(id);
    }

    @PostMapping
    public CreateAccountResponse createAccount(
            @RequestBody CreateAccountRequest request
    ) {
        log.info("Initiating request to create account, req: {}", request);
        return accountService.createAccount(request);
    }

}
