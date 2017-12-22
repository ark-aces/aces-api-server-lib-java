package com.arkaces.aces_server.ark_auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AccountUpdateWorker {

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @Scheduled(cron = "0 0 6 * * *") // 6 am every day
    public void run() {
        log.info("Updating API statuses");
        List<AccountEntity> accountEntities = accountRepository
                .findAllByStatusIn(Arrays.asList(AccountStatus.ACTIVE, AccountStatus.PENDING));
        for (AccountEntity accountEntity : accountEntities) {
            accountService.updateStatus(accountEntity);
        }
    }

}
