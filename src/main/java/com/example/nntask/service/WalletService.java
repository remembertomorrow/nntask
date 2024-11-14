package com.example.nntask.service;

import com.example.nntask.model.dto.WalletDto;
import com.example.nntask.model.entity.Account;
import com.example.nntask.model.entity.Wallet;
import com.example.nntask.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {


    private final WalletRepository walletRepository;


    public Wallet buildWallet(Currency currency, BigDecimal initialBalance, Account account) {
        Wallet defaultWallet = new Wallet();
        defaultWallet.setWalletCurrency(currency);
        defaultWallet.setAmount(initialBalance);
        defaultWallet.setAccount(account);
        return defaultWallet;
    }


    public Wallet updateWalletBalance(Wallet wallet, BigDecimal newBalance) {
        wallet.setAmount(newBalance);
        return walletRepository.save(wallet);
    }


    public List<WalletDto> mapWalletsToDtos(List<Wallet> wallets) {
        return wallets.stream().map(w -> {
            WalletDto dto = new WalletDto();
            dto.setWalletId(w.getId());
            dto.setWalletCurrency(w.getWalletCurrency());
            dto.setAmount(w.getAmount());
            dto.setAccountId(w.getAccount().getId());
            return dto;
        }).toList();
    }

}
