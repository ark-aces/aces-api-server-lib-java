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

    // todo: store bcrypt hash of api key
    private String apiKey;

    private String userArkAddress;

    private Boolean userArkAddressVerified;

    private String paymentArkAddress;

    // todo: store account password encrypted
    private String paymentArkPassphrase;

    private String status;

    private BigDecimal arkStake;

    private Boolean hasEnoughStake;

    private BigDecimal paymentAccountAmount;

    private Boolean hasPaidFee;

    private LocalDateTime createdAt;

}
