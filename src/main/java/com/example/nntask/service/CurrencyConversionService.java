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

    private static final String PLN = "PLN";

    @Value("${wallets.supported-currencies}")
    private String supportedCurrencies;

    private final AccountService accountService;

    private final WalletTransactionService walletTransactionService;


    public GetAccountResponse convertCurrency(ConvertCurrencyRequest req) {

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

        Account account = accountService.findAccount(req.getAccountId());
        Wallet plnWallet = chooseWalletByCurrency(account, PLN);
        Wallet otherWallet = chooseWalletByCurrency(account, otherCurrencyCode);
        walletTransactionService.moveWalletsFunds(plnWallet, otherWallet, req);

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
