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
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final String arkAuthServiceArkAddress;
    private final BigDecimal arkAuthMinArkStake;
    private final BigDecimal arkAuthArkFee;
    private final ArkClient arkAuthArkClient; 

    public void updateStatus(AccountEntity accountEntity) {
        log.info("Updating API key status for api key " + accountEntity.getId());

        String userArkAddress = accountEntity.getUserArkAddress();

        if (! accountEntity.getUserArkAddressVerified()) {
            // Attempt verification by checking payment transactions for user ark address
            boolean isVerified = false;
            String paymentArkAddress = accountEntity.getPaymentArkAddress();
            List<Transaction> transactions = arkAuthArkClient.getTransactionByRecipientAddress(paymentArkAddress, 50, 0);
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

        // Get user account balance 
        AccountBalance accountBalance = arkAuthArkClient.getBalance(userArkAddress);
        BigDecimal userArkAmount = new BigDecimal(accountBalance.getBalance())
                .setScale(14, BigDecimal.ROUND_UP)
                .divide(new BigDecimal(ArkConstants.SATOSHIS_PER_ARK), BigDecimal.ROUND_UP);
        accountEntity.setArkStake(userArkAmount);

        // Check user account ark stake
        if (arkAuthMinArkStake.compareTo(BigDecimal.ZERO) == 0 
            || (accountEntity.getUserArkAddressVerified() && userArkAmount.compareTo(arkAuthMinArkStake) >= 0)) {
            accountEntity.setHasEnoughStake(true);
        } else {
            accountEntity.setHasEnoughStake(false);
        }
        
        // Check account fee payment
        if (arkAuthArkFee.compareTo(BigDecimal.ZERO) > 0) {
            // Charge fee by making a payment from payment address to service address
            
            // Get payment balance
            String paymentArkAddress = accountEntity.getPaymentArkAddress();
            AccountBalance paymentAccountBalance = arkAuthArkClient.getBalance(paymentArkAddress);
            BigDecimal paymentAccountAmount = new BigDecimal(paymentAccountBalance.getBalance())
                .setScale(14, BigDecimal.ROUND_UP)
                .divide(new BigDecimal(ArkConstants.SATOSHIS_PER_ARK), BigDecimal.ROUND_UP);
            accountEntity.setPaymentAccountAmount(paymentAccountAmount);
            
            if (paymentAccountAmount.compareTo(arkAuthArkFee) >= 0) {
                // todo: save fee charge transaction information to database
                Long satoshiAmount = arkAuthArkFee
                    .multiply(new BigDecimal(ArkConstants.SATOSHIS_PER_ARK))
                    .toBigIntegerExact()
                    .longValue();

                String transactionId = arkAuthArkClient
                    .broadcastTransaction(
                        arkAuthServiceArkAddress,
                        satoshiAmount,
                        "ACES API key activation fee " + LocalDateTime.now().toString(),
                        accountEntity.getPaymentArkPassphrase(),
                        10
                    );

                // todo: what happens when payment wallet doesn't have enough funds
                log.info("Broadcasted fee payment transaction " + transactionId);

                accountEntity.setHasPaidFee(true);
            } else {
                accountEntity.setHasPaidFee(false);
            }
        } else {
            accountEntity.setHasPaidFee(true);
        }

        if (accountEntity.getHasEnoughStake() && accountEntity.getHasPaidFee()) {
            accountEntity.setStatus(AccountStatus.ACTIVE);
        }

        accountRepository.save(accountEntity);
    }

}
