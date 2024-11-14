package com.example.nntask.model.response;

import com.example.nntask.model.dto.WalletDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateAccountResponse {

    private UUID accountId;

    private String firstName;

    private String lastName;

    private List<WalletDto> wallets;

}
