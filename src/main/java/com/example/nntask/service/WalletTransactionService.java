package com.example.nntask.service;

import com.example.nntask.exception.CurrencyConversionException;
import com.example.nntask.integration.NbpCurrencyRateProviderClient;
import com.example.nntask.model.entity.Wallet;
import com.example.nntask.model.request.ConvertCurrencyRequest;
import com.example.nntask.model.response.NbpExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletTransactionService {

    @Value("${conversion.base-currency}")
    private String baseCurrencyCode;

    private final WalletService walletService;

    private final NbpCurrencyRateProviderClient nbpProvider;


    @Transactional
    public void moveWalletsFunds(Wallet baseCurrencyWallet, Wallet otherWallet, ConvertCurrencyRequest req) {

        boolean isBaseCurrencyOrigin = req.getOriginCurrency().equals(baseCurrencyCode);
        String otherCurrencyCode = isBaseCurrencyOrigin ? req.getTargetCurrency() : req.getOriginCurrency();
        NbpExchangeRateResponse rate = getCurrencyRate(otherCurrencyCode);

        Wallet originWallet = isBaseCurrencyOrigin ? baseCurrencyWallet : otherWallet;
        Wallet targetWallet = isBaseCurrencyOrigin ? otherWallet : baseCurrencyWallet;

        if(!isEnoughFundsInOrigin(originWallet, req)) {
            throw new CurrencyConversionException("Not enough funds in the origin currency wallet");
        }

        log.info("Converting funds: {}, from: {} wallet to: {} wallet",
                req.getOriginAmount(), req.getOriginCurrency(), req.getTargetCurrency());

        walletService.updateWalletBalance(
                originWallet, originWallet.getAmount().subtract(req.getOriginAmount())
        );
        BigDecimal amountToAdd = isBaseCurrencyOrigin ?
                req.getOriginAmount().divide(BigDecimal.valueOf(rate.getRates().getFirst().getAsk()), 4, RoundingMode.HALF_EVEN) :
                        req.getOriginAmount().multiply(BigDecimal.valueOf(rate.getRates().getFirst().getBid()));
        walletService.updateWalletBalance(targetWallet, targetWallet.getAmount().add(amountToAdd));

    }


    public NbpExchangeRateResponse getCurrencyRate(String currencyCode) {
        NbpExchangeRateResponse rate = nbpProvider.getTodayExchangeRate(currencyCode);
        log.info("Fetched currency rates response: {}", rate);
        if(rate == null || rate.getRates().isEmpty()) {
            throw new CurrencyConversionException("There was a problem fetching the currency rate");
        }
        return rate;
    }


    private boolean isEnoughFundsInOrigin(Wallet originWallet, ConvertCurrencyRequest req) {
        return originWallet.getAmount().compareTo(req.getOriginAmount()) >= 0;
    }


}
