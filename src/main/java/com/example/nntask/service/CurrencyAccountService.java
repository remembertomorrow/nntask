package com.example.nntask.service;

import com.example.nntask.exception.CurrencyAccountNotFoundException;
import com.example.nntask.model.entity.CurrencyAccount;
import com.example.nntask.model.entity.Wallet;
import com.example.nntask.model.request.CreateCurrencyAccountRequest;
import com.example.nntask.model.response.CreateCurrencyAccountResponse;
import com.example.nntask.model.response.GetCurrencyAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.nntask.repository.CurrencyAccountRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CurrencyAccountService {

    private static final String PLN = "PLN";

    @Value("${wallets.supported-currencies}")
    private String supportedCurrencies;

    private final CurrencyAccountRepository currencyAccountRepository;

    private final WalletService walletService;


    public CreateCurrencyAccountResponse createCurrencyAccount(CreateCurrencyAccountRequest request) {

        CurrencyAccount account = new CurrencyAccount();
        account.setFirstName(request.getFirstName());
        account.setLastName(request.getLastName());
        Arrays.stream(supportedCurrencies.split(",")).forEach(c -> {
            account.getWallets().add(walletService.buildWallet(
                    Currency.getInstance(c),
                    c.equals(PLN) ? request.getInitialBalance() : BigDecimal.ZERO,
                    account
            ));
        });
        currencyAccountRepository.save(account);

        return new CreateCurrencyAccountResponse(
                account.getId(),
                account.getFirstName(),
                account.getLastName(),
                walletService.mapWalletsToDtos(account.getWallets())
        );
    }


    public GetCurrencyAccountResponse getCurrencyAccountById(UUID accountId) {
        CurrencyAccount currencyAccount = findCurrencyAccount(accountId);
        return mapAccountToGetAccountResponse(currencyAccount);
    }


    public CurrencyAccount findCurrencyAccount(UUID accountId) {
        return currencyAccountRepository.findById(accountId).orElseThrow(
                () -> new CurrencyAccountNotFoundException(String.format("Couldn't find account with id: %s", accountId))
        );
    }

    public GetCurrencyAccountResponse mapAccountToGetAccountResponse(CurrencyAccount currencyAccount) {
        GetCurrencyAccountResponse response = new GetCurrencyAccountResponse();
        response.setAccountId(currencyAccount.getId());
        response.setFirstName(currencyAccount.getFirstName());
        response.setLastName(currencyAccount.getLastName());
        response.setWallets(currencyAccount.getWallets());
        return response;
    }


}
