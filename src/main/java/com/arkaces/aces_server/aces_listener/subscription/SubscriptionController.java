package com.arkaces.aces_server.aces_listener.subscription;

import com.arkaces.aces_server.common.identifer.IdentifierGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@RestController
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubscriptionController {

    private final IdentifierGenerator identifierGenerator;
    private final SubscriptionRepository subscriptionRepository;

    @PostMapping("/subscriptions")
    public Subscription postSubscription(@RequestBody CreateSubscriptionRequest createSubscriptionRequest) {
        // todo: validate request body

        String identifier = identifierGenerator.generate();

        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
        subscriptionEntity.setId(identifier);
        subscriptionEntity.setCallbackUrl(createSubscriptionRequest.getCallbackUrl());
        subscriptionEntity.setMinConfirmations(createSubscriptionRequest.getMinConfirmations());
        subscriptionEntity.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC));
        subscriptionEntity.setStatus(SubscriptionStatus.ACTIVE);
        subscriptionRepository.save(subscriptionEntity);

        Subscription subscription = new Subscription();
        subscription.setStatus(subscriptionEntity.getStatus());
        subscription.setId(subscriptionEntity.getId());
        subscription.setCallbackUrl(subscriptionEntity.getCallbackUrl());
        subscription.setMinConfirmations(subscriptionEntity.getMinConfirmations());
        subscription.setCreatedAt(subscriptionEntity.getCreatedAt().toString());

        return subscription;
    }

}