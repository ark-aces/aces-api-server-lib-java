package com.arkaces.aces_server.aces_listener.subscription;

import lombok.Data;

@Data
public class Subscription {
    private String id;
    private String status;
    private String callbackUrl;
    private Integer minConfirmations;
    private String createdAt;
}
