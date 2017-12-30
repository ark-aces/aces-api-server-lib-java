package com.arkaces.aces_server.ark_auth.security;

import com.arkaces.aces_server.ark_auth.Account;
import com.arkaces.aces_server.ark_auth.AccountEntity;
import com.arkaces.aces_server.ark_auth.AccountMapper;
import com.arkaces.aces_server.ark_auth.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account;
        AccountEntity accountEntity = accountRepository.findOneById(username);
        if (accountEntity == null) {
            throw new UsernameNotFoundException("User not found");
        } else {
            account = accountMapper.map(accountEntity);
        }
        
        return new com.arkaces.aces_server.ark_auth.security.User(
            account.getId(),
            account.getApiKey(), 
            AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ACCOUNT_USER"),
            account
        );
    }
}
