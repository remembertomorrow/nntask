package com.example.nntask.repository;

import com.example.nntask.model.entity.CurrencyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CurrencyAccountRepository extends JpaRepository<CurrencyAccount, UUID> {

}
