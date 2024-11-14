package com.example.nntask;

import com.example.nntask.exception.CurrencyConversionException;
import com.example.nntask.fixture.FixtureBuilder;
import com.example.nntask.integration.NbpCurrencyRateProviderClient;
import com.example.nntask.model.response.NbpExchangeRateResponse;
import com.example.nntask.repository.AccountRepository;
import com.example.nntask.service.WalletService;
import com.example.nntask.service.WalletTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WalletTransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private WalletService walletService;

    @Mock
    private NbpCurrencyRateProviderClient nbpProvider;

    @InjectMocks
    private WalletTransactionService walletTransactionService;

    @BeforeEach
    void setUp() throws Exception {
        Field baseCurrencyCodeField = WalletTransactionService.class.getDeclaredField("baseCurrencyCode");
        baseCurrencyCodeField.setAccessible(true);
        baseCurrencyCodeField.set(walletTransactionService, "PLN");
    }


    @Test
    @DisplayName("Getting currency rate success")
    void getCurrencyRate_Success() {
        String currencyCode = "USD";
        NbpExchangeRateResponse nbpRate = FixtureBuilder.buildNbpExchangeRateResponse();

        when(nbpProvider.getTodayExchangeRate(currencyCode)).thenReturn(nbpRate);
        NbpExchangeRateResponse response = walletTransactionService.getCurrencyRate(currencyCode);

        assertNotNull(response);
    }


    @Test
    @DisplayName("Getting currency rate failure - nbp response null")
    void getCurrencyRate_Failure() {
        String currencyCode = "USD";
        when(nbpProvider.getTodayExchangeRate(currencyCode)).thenReturn(null);
        assertThrows(CurrencyConversionException.class, () ->
            walletTransactionService.getCurrencyRate(currencyCode)
        );
    }

}