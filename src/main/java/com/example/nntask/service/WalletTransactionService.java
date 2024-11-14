package com.example.nntask.service;

import com.example.nntask.exception.CurrencyConversionException;
import com.example.nntask.integration.NbpFeignClient;
import com.example.nntask.model.entity.Wallet;
import com.example.nntask.model.request.ConvertCurrencyRequest;
import com.example.nntask.model.response.NbpExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class WalletTransactionService {

    private static final String PLN = "PLN";

    private static final String NBP_BID_ASK_TABLE = "C";

    private final WalletService walletService;

    private final NbpFeignClient nbpFeignClient;


    @Transactional
    public void moveWalletsFunds(Wallet plnWallet, Wallet otherWallet, ConvertCurrencyRequest req) {

        boolean isPlnOrigin = req.getOriginCurrency().equals(PLN);
        String otherCurrencyCode = isPlnOrigin ? req.getTargetCurrency() : req.getOriginCurrency();
        NbpExchangeRateResponse rate = getCurrencyRate(otherCurrencyCode);

        Wallet originWallet = isPlnOrigin ? plnWallet : otherWallet;
        Wallet targetWallet = isPlnOrigin ? otherWallet : plnWallet;

        if(!isEnoughFundsInOrigin(originWallet, req)) {
            throw new CurrencyConversionException("Not enough funds in the origin currency wallet");
        }

        walletService.updateWalletBalance(
                originWallet, originWallet.getAmount().subtract(req.getOriginAmount())
        );
        BigDecimal amountToAdd = isPlnOrigin ?
                req.getOriginAmount().divide(BigDecimal.valueOf(rate.getRates().getFirst().getAsk()), 4, RoundingMode.HALF_EVEN) :
                        req.getOriginAmount().multiply(BigDecimal.valueOf(rate.getRates().getFirst().getBid()));
        walletService.updateWalletBalance(targetWallet, targetWallet.getAmount().add(amountToAdd));

    }


    public NbpExchangeRateResponse getCurrencyRate(String currencyCode) {
        NbpExchangeRateResponse rate = nbpFeignClient.getTodayExchangeRate(NBP_BID_ASK_TABLE, currencyCode);
        if(rate == null || rate.getRates().isEmpty()) {
            throw new CurrencyConversionException("There was a problem fetching the currency rate");
        }
        return rate;
    }


    private boolean isEnoughFundsInOrigin(Wallet originWallet, ConvertCurrencyRequest req) {
        return originWallet.getAmount().compareTo(req.getOriginAmount()) >= 0;
    }


}
