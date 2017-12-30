package com.arkaces.aces_server.ark_auth.security;

import org.springframework.security.core.AuthenticationException;

public class InactiveApiKeyException extends AuthenticationException {
    public InactiveApiKeyException(String s) {
        super(s);
    }
}
