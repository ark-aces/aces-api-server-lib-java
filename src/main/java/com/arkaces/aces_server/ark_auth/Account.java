package com.arkaces.aces_server.ark_auth;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Account {
    private String id;
    private String status;
    private String apiKey;
    private String userArkAddress;
    private String paymentArkAddress;
    private String createdAt;
    private Boolean hasEnoughStake;
    private Boolean hasPaidFee;
    private Boolean userArkAddressVerified;
    private BigDecimal arkStake;
    private BigDecimal paymentAccountAmount;
}
