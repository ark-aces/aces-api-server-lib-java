package com.arkaces.aces_server.ark_auth.security;

import com.arkaces.aces_server.ark_auth.AccountEntity;
import com.arkaces.aces_server.ark_auth.AccountRepository;
import com.arkaces.aces_server.ark_auth.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomAuthenticationProvider implements AuthenticationProvider {
    
    private final AccountRepository accountRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String apiKey = (String) authentication.getCredentials();

        AccountEntity accountEntity = accountRepository.findOneByStatusAndApiKey(AccountStatus.ACTIVE, apiKey);
        if (accountEntity == null) {
            throw new BadCredentialsException("Authentication credentials invalid");
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ACCOUNT_USER"));

        return new UsernamePasswordAuthenticationToken(username, apiKey, authorities);
    }

    @Override
    public boolean supports(Class<?> arg0) {
        return true;
    }
}
