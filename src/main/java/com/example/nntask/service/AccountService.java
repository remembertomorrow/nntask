package com.example.nntask.service;

import com.example.nntask.exception.AccountNotFoundException;
import com.example.nntask.model.entity.Account;
import com.example.nntask.model.request.CreateAccountRequest;
import com.example.nntask.model.response.CreateAccountResponse;
import com.example.nntask.model.response.GetAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.nntask.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AccountService {

    private static final String PLN = "PLN";

    @Value("${wallets.supported-currencies}")
    private String supportedCurrencies;

    private final AccountRepository accountRepository;

    private final WalletService walletService;


    public CreateAccountResponse createAccount(CreateAccountRequest request) {

        Account account = new Account();
        account.setFirstName(request.getFirstName());
        account.setLastName(request.getLastName());
        Arrays.stream(supportedCurrencies.split(",")).forEach(c -> {
            account.getWallets().add(walletService.buildWallet(
                    Currency.getInstance(c),
                    c.equals(PLN) ? request.getInitialBalance() : BigDecimal.ZERO,
                    account
            ));
        });
        accountRepository.save(account);

        return new CreateAccountResponse(
                account.getId(),
                account.getFirstName(),
                account.getLastName(),
                walletService.mapWalletsToDtos(account.getWallets())
        );
    }


    public GetAccountResponse getAccountById(UUID accountId) {
        Account account = findAccount(accountId);
        return mapAccountToGetAccountResponse(account);
    }


    public Account findAccount(UUID accountId) {
        return accountRepository.findById(accountId).orElseThrow(
                () -> new AccountNotFoundException(String.format("Couldn't find account with id: %s", accountId))
        );
    }

    public GetAccountResponse mapAccountToGetAccountResponse(Account account) {
        GetAccountResponse response = new GetAccountResponse();
        response.setAccountId(account.getId());
        response.setFirstName(account.getFirstName());
        response.setLastName(account.getLastName());
        response.setWallets(account.getWallets());
        return response;
    }


}
