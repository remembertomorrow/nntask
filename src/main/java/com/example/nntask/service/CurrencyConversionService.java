package com.example.nntask.service;

import com.example.nntask.exception.CurrencyConversionException;
import com.example.nntask.model.entity.Account;
import com.example.nntask.model.entity.Wallet;
import com.example.nntask.model.request.ConvertCurrencyRequest;
import com.example.nntask.model.response.GetAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyConversionService {

    @Value("${conversion.base-currency}")
    private String baseCurrencyCode;

    @Value("${wallets.supported-currencies}")
    private String supportedCurrencies;

    private final AccountService accountService;

    private final WalletTransactionService walletTransactionService;


    public GetAccountResponse convertCurrency(ConvertCurrencyRequest req) {

        boolean isBaseCurrencyOrigin = req.getOriginCurrency().equals(baseCurrencyCode);
        String otherCurrencyCode = isBaseCurrencyOrigin ? req.getTargetCurrency() : req.getOriginCurrency();

        if(!isOriginAmountValid(req)) {
            throw new CurrencyConversionException("Origin amount invalid");
        }
        if(!isExactlyOneCurrencyBase(req)) {
            throw new CurrencyConversionException(
                    String.format("Incorrect currency pair, exactly one of the currencies needs to be %s", baseCurrencyCode));
        }
        if(!isSupportedCurrency(otherCurrencyCode)) {
            throw new CurrencyConversionException(
                    String.format("Currency is not supported. Supported currencies are: %s", supportedCurrencies));
        }

        Account account = accountService.findAccount(req.getAccountId());
        Wallet baseCurrencyWallet = chooseWalletByCurrency(account, baseCurrencyCode);
        Wallet otherWallet = chooseWalletByCurrency(account, otherCurrencyCode);
        walletTransactionService.moveWalletsFunds(baseCurrencyWallet, otherWallet, req);

        return accountService.mapAccountToGetAccountResponse(account);
    }


    private Wallet chooseWalletByCurrency(Account account, String currencyCode) {
        List<Wallet> wallets = account.getWallets();
        if(wallets.isEmpty()) {
            throw new CurrencyConversionException("Account doesn't have any wallets");
        }
        return wallets.stream().filter(w -> w.getWalletCurrency().getCurrencyCode().equals(currencyCode))
                .toList().getFirst();
    }

    private boolean isExactlyOneCurrencyBase(ConvertCurrencyRequest req) {
        return (req.getOriginCurrency().equals(baseCurrencyCode) || req.getTargetCurrency().equals(baseCurrencyCode)) &&
                !(req.getOriginCurrency().equals(baseCurrencyCode) && req.getTargetCurrency().equals(baseCurrencyCode));
    }

    private boolean isSupportedCurrency(String currencyCode) {
        return Arrays.asList(supportedCurrencies.split(",")).contains(currencyCode);
    }

    private boolean isOriginAmountValid(ConvertCurrencyRequest req) {
        return req.getOriginAmount().compareTo(BigDecimal.valueOf(0.01)) >= 0;
    }

}
