package com.example.nntask.model.response;

import com.example.nntask.model.entity.Wallet;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class GetAccountResponse {

    private UUID accountId;

    private String firstName;

    private String lastName;

    private List<Wallet> wallets = new ArrayList<>();

}
