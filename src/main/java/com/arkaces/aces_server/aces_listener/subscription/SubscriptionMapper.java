package com.arkaces.aces_server.aces_listener.subscription;

import org.springframework.stereotype.Service;

import java.time.ZoneOffset;

@Service
public class SubscriptionMapper {

    public Subscription map(SubscriptionEntity subscriptionEntity) {
        Subscription subscription = new Subscription();
        subscription.setStatus(subscriptionEntity.getStatus());
        subscription.setId(subscriptionEntity.getId());
        subscription.setCallbackUrl(subscriptionEntity.getCallbackUrl());
        subscription.setMinConfirmations(subscriptionEntity.getMinConfirmations());
        subscription.setCreatedAt(subscriptionEntity.getCreatedAt().atOffset(ZoneOffset.UTC).toString());

        return subscription;
    }
}
