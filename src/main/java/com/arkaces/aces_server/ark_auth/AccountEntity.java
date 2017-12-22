package com.arkaces.aces_server.ark_auth;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "accounts")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    private String id;

    private String apiKey;

    private String userArkAddress;

    private Boolean userArkAddressVerified;

    private String paymentArkAddress;

    private String paymentArkPassphrase;

    private LocalDateTime nextChargeAt;

    private String status;

    private BigDecimal arkStake;

    private Boolean hasEnoughStake;

    private BigDecimal paymentAccountAmount;

    private Boolean hasPaidFee;

    private LocalDateTime createdAt;

}
