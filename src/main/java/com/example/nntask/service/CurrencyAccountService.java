package com.example.nntask.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.nntask.repository.CurrencyAccountRepository;

@Service
@RequiredArgsConstructor
public class CurrencyAccountService {

    private final CurrencyAccountRepository currencyAccountRepository;


}
