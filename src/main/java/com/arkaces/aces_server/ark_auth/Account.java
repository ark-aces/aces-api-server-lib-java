package com.arkaces.aces_server.ark_auth;

import lombok.Data;

@Data
public class Account {
    private String id;
    private String status;
    private String apiKey;
    private String userArkAddress;
    private String paymentArkAddress;
    private String createdAt;
}
