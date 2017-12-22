package com.arkaces.aces_server.ark_auth.security;

import com.arkaces.aces_server.ark_auth.Account;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class User extends org.springframework.security.core.userdetails.User {

    private final Account account;

    public User(String username, String password, Collection<? extends GrantedAuthority> authorities, Account account) {
        super(username, password, authorities);
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }
}
