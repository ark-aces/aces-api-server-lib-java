package com.arkaces.aces_server.aces_listener.subscription;

import com.arkaces.aces_server.common.error.ErrorCodes;
import com.arkaces.aces_server.common.error.NotFoundException;
import com.arkaces.aces_server.common.identifer.IdentifierGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@RestController
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubscriptionController {

    private final IdentifierGenerator identifierGenerator;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

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

        return subscriptionMapper.map(subscriptionEntity);
    }

    @GetMapping("/subscriptions/{id}")
    public Subscription getSubscription(@PathVariable String id) {
        SubscriptionEntity subscriptionEntity = subscriptionRepository.findOneById(id);
        if (subscriptionEntity == null) {
            throw new NotFoundException(ErrorCodes.SUBSCRIPTION_NOT_FOUND, "Subscription not found");
        }

        return subscriptionMapper.map(subscriptionEntity);
    }

}
