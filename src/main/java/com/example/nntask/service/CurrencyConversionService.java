package com.example.nntask.service;

import com.example.nntask.exception.CurrencyConversionException;
import com.example.nntask.integration.NbpFeignClient;
import com.example.nntask.model.entity.CurrencyAccount;
import com.example.nntask.model.entity.Wallet;
import com.example.nntask.model.request.ConvertCurrencyRequest;
import com.example.nntask.model.response.ConvertCurrencyResponse;
import com.example.nntask.model.response.GetCurrencyAccountResponse;
import com.example.nntask.model.response.NbpExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CurrencyConversionService {

    private static final String PLN = "PLN";

    @Value("${wallets.supported-currencies}")
    private String supportedCurrencies;

    private final CurrencyAccountService currencyAccountService;

    private final WalletTransactionService walletTransactionService;


    public GetCurrencyAccountResponse convertCurrency(ConvertCurrencyRequest req) {

        boolean isPlnOrigin = req.getOriginCurrency().equals(PLN);
        String otherCurrencyCode = isPlnOrigin ? req.getTargetCurrency() : req.getOriginCurrency();

        if(!isOriginAmountValid(req)) {
            throw new CurrencyConversionException("Origin amount invalid");
        }
        if(!isExactlyOneCurrencyPLN(req)) {
            throw new CurrencyConversionException("Incorrect currency pair, exactly one of the currencies needs to be PLN");
        }
        if(!isSupportedCurrency(otherCurrencyCode)) {
            throw new CurrencyConversionException(
                    String.format("Currency is not supported. Supported currencies are: %s", supportedCurrencies));
        }

        CurrencyAccount account = currencyAccountService.findCurrencyAccount(req.getAccountId());
        Wallet plnWallet = chooseWalletByCurrency(account, PLN);
        Wallet otherWallet = chooseWalletByCurrency(account, otherCurrencyCode);
        walletTransactionService.moveWalletsFunds(plnWallet, otherWallet, req);

        return currencyAccountService.mapAccountToGetAccountResponse(account);
    }


    private Wallet chooseWalletByCurrency(CurrencyAccount account, String currencyCode) {
        List<Wallet> wallets = account.getWallets();
        if(wallets.isEmpty()) {
            throw new CurrencyConversionException("Account doesn't have any wallets");
        }
        return wallets.stream().filter(w -> w.getWalletCurrency().getCurrencyCode().equals(currencyCode))
                .toList().getFirst();
    }

    private boolean isExactlyOneCurrencyPLN(ConvertCurrencyRequest req) {
        return (req.getOriginCurrency().equals(PLN) || req.getTargetCurrency().equals(PLN)) &&
                !(req.getOriginCurrency().equals(PLN) && req.getTargetCurrency().equals(PLN));
    }

    private boolean isSupportedCurrency(String currencyCode) {
        return Arrays.asList(supportedCurrencies.split(",")).contains(currencyCode);
    }

    private boolean isOriginAmountValid(ConvertCurrencyRequest req) {
        return req.getOriginAmount().compareTo(BigDecimal.valueOf(0.01)) >= 0;
    }

}
