package com.arkaces.aces_server.ark_auth;

import org.springframework.stereotype.Service;

import java.time.ZoneOffset;

@Service
public class AccountMapper {

    public Account map(AccountEntity accountEntity) {
        Account account = new Account();
        account.setId(accountEntity.getId());
        account.setApiKey(accountEntity.getApiKey());
        account.setStatus(accountEntity.getStatus());
        account.setUserArkAddress(accountEntity.getUserArkAddress());
        account.setPaymentArkAddress(accountEntity.getPaymentArkAddress());
        account.setCreatedAt(accountEntity.getCreatedAt().atOffset(ZoneOffset.UTC).toString());

        return account;
    }
}
