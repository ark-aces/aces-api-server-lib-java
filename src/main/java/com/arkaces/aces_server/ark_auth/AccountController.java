package com.arkaces.aces_server.ark_auth;

import com.arkaces.aces_server.common.api_key_generation.ApiKeyGenerator;
import com.arkaces.aces_server.common.identifer.IdentifierGenerator;
import io.ark.core.Crypto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountController {

    private final ApiKeyGenerator apiKeyGenerator;
    private final IdentifierGenerator identifierGenerator;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @PostMapping("/accounts")
    public Account postAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        String identifier = identifierGenerator.generate();

        // generate payment ark account
        String paymentArkPassphrase = apiKeyGenerator.generate();
        String paymentArkAddress = Crypto.getAddress(Crypto.getKeys(paymentArkPassphrase));

        String userArkAddress = createAccountRequest.getUserArkAddress();

        String token = apiKeyGenerator.generate();

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(identifier);
        accountEntity.setUserArkAddress(userArkAddress);
        accountEntity.setUserArkAddressVerified(false);
        accountEntity.setPaymentArkAddress(paymentArkAddress);
        accountEntity.setPaymentArkPassphrase(paymentArkPassphrase);
        accountEntity.setStatus(AccountStatus.PENDING);
        accountEntity.setApiKey(token);
        accountEntity.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));

        accountRepository.save(accountEntity);

        return accountMapper.map(accountEntity);
    }

}
