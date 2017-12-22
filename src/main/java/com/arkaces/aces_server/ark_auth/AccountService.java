package com.arkaces.aces_server.ark_auth;

import ark_java_client.AccountBalance;
import ark_java_client.ArkClient;
import ark_java_client.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final String arkAuthorizationServiceArkAddress;
    private final ArkClient arkAuthorizationArkClient;
    private final BigDecimal arkAuthorizationMinArkStake;
    private final BigDecimal arkAuthorizationArkFee;
    private final ArkClient localArkClient;

    public void updateStatus(AccountEntity accountEntity) {
        log.info("Updating API key status for api key " + accountEntity.getId());

        String userArkAddress = accountEntity.getUserArkAddress();

        if (! accountEntity.getUserArkAddressVerified()) {
            // Attempt verification by checking payment transactions for user ark address
            boolean isVerified = false;
            String paymentArkAddress = accountEntity.getPaymentArkAddress();
            List<Transaction> transactions = localArkClient.getTransactionByRecipientAddress(paymentArkAddress);
            for (Transaction transaction : transactions) {
                if (transaction.getSenderId().equals(userArkAddress)) {
                    // todo: we should check confirmations so we don't verify for low-confirmation transactions
                    isVerified = true;
                }
            }
            if (isVerified) {
                accountEntity.setUserArkAddressVerified(true);
            }
        }

        if (accountEntity.getUserArkAddressVerified()) {
            AccountBalance accountBalance = localArkClient.getBalance(userArkAddress);
            BigDecimal userArkAmount = new BigDecimal(accountBalance.getBalance())
                    .setScale(14, BigDecimal.ROUND_UP)
                    .divide(new BigDecimal(ArkConstants.SATOSHIS_PER_ARK), BigDecimal.ROUND_UP);
            accountEntity.setArkStake(userArkAmount);

            String paymentArkAddress = accountEntity.getPaymentArkAddress();
            AccountBalance paymentAccountBalance = localArkClient.getBalance(paymentArkAddress);
            BigDecimal paymentAccountAmount = new BigDecimal(paymentAccountBalance.getBalance())
                    .setScale(14, BigDecimal.ROUND_UP)
                    .divide(new BigDecimal(ArkConstants.SATOSHIS_PER_ARK), BigDecimal.ROUND_UP);
            accountEntity.setPaymentAccountAmount(paymentAccountAmount);

            // if address doesn't have enough ARK stake, inactivate api key
            if (userArkAmount.compareTo(arkAuthorizationMinArkStake) < 0) {
                accountEntity.setStatus(AccountStatus.PENDING);
                accountEntity.setHasEnoughStake(false);
                accountEntity.setHasPaidFee(false);
            } else {
                accountEntity.setHasEnoughStake(true);

                // charge activation fee
                if (arkAuthorizationArkFee.compareTo(BigDecimal.ZERO) > 0) {
                    Long satoshiAmount = arkAuthorizationArkFee
                            .multiply(new BigDecimal(ArkConstants.SATOSHIS_PER_ARK))
                            .toBigIntegerExact()
                            .longValue();
                    // todo: what happens when payment wallet doesn't have enough funds
                    // todo: save fee charge transaction information to database
                    String transactionId = arkAuthorizationArkClient
                            .createTransaction(
                                    arkAuthorizationServiceArkAddress,
                                    satoshiAmount,
                                    "ACES API key activation fee",
                                    accountEntity.getPaymentArkPassphrase()
                            );

                    accountEntity.setHasPaidFee(true);

                    // activate key!
                    accountEntity.setStatus(AccountStatus.ACTIVE);
                } else {
                    // if fee is 0, just activate
                    accountEntity.setStatus(AccountStatus.ACTIVE);
                }
            }
        }

        accountRepository.save(accountEntity);
    }

}
