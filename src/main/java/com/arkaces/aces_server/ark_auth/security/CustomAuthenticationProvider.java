package com.arkaces.aces_server.ark_auth.security;

import com.arkaces.aces_server.ark_auth.AccountEntity;
import com.arkaces.aces_server.ark_auth.AccountRepository;
import com.arkaces.aces_server.ark_auth.AccountService;
import com.arkaces.aces_server.ark_auth.AccountStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
    
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("authentication entry point: " + authentication.toString());
        String apiKey = (String) authentication.getCredentials();
        AccountEntity accountEntity = accountRepository.findOneByApiKey(apiKey);
        if (accountEntity == null) {
            throw new BadCredentialsException("Bad Credentials");
        }
        
        // try to activate the api key if it's inactive
        if (! accountEntity.getStatus().equals(AccountStatus.ACTIVE)) {
            accountService.updateStatus(accountEntity);
        }
        
        if (! accountEntity.getStatus().equals(AccountStatus.ACTIVE)) {
            throw new InactiveApiKeyException("Inactive API Key");
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ACCOUNT_USER"));

        String username = accountEntity.getId();

        return new UsernamePasswordAuthenticationToken(username, apiKey, authorities);
    }

    @Override
    public boolean supports(Class<?> arg0) {
        return true;
    }
}
