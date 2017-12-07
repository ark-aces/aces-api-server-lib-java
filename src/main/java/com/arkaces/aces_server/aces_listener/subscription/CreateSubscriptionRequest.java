package com.arkaces.aces_server.aces_listener.subscription;

import lombok.Data;

@Data
public class CreateSubscriptionRequest {
    private String callbackUrl;
    private Integer minConfirmations;
}
