package com.example.nntask.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Wallet extends BaseEntity {

    @Column(name = "wallet_currency")
    private Currency walletCurrency;

    @Column(name = "amount", precision = 10, scale = 4)
    private BigDecimal amount;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="account_id", nullable=false)
    private Account account;

}
