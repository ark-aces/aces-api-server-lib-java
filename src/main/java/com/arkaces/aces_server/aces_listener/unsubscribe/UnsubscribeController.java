package com.arkaces.aces_server.aces_listener.unsubscribe;

import com.arkaces.aces_server.aces_listener.subscription.SubscriptionEntity;
import com.arkaces.aces_server.aces_listener.subscription.SubscriptionRepository;
import com.arkaces.aces_server.aces_listener.subscription.SubscriptionStatus;
import com.arkaces.aces_server.common.error.NotFoundException;
import com.arkaces.aces_server.common.identifer.IdentifierGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class UnsubscribeController {

    private final IdentifierGenerator identifierGenerator;
    private final SubscriptionRepository subscriptionRepository;
    private final UnsubscribeRepository unsubscribeRepository;

    @PostMapping("/subscriptions/{subscriptionId}/unsubscribes")
    public Unsubscribe postUnsubscribe(@PathVariable String subscriptionId) {
        SubscriptionEntity subscriptionEntity = subscriptionRepository.findOneById(subscriptionId);
        if (subscriptionEntity == null) {
            throw new NotFoundException("SubscriptionNotFound", "Subscription not found.");
        }

        UnsubscribeEntity unsubscribeEntity = new UnsubscribeEntity();
        unsubscribeEntity.setId(identifierGenerator.generate());
        unsubscribeEntity.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        unsubscribeEntity.setSubscriptionEntity(subscriptionEntity);
        unsubscribeRepository.save(unsubscribeEntity);

        subscriptionEntity.setStatus(SubscriptionStatus.UNSUBSCRIBED);
        subscriptionRepository.save(subscriptionEntity);

        Unsubscribe unsubscribe = new Unsubscribe();
        unsubscribe.setId(unsubscribeEntity.getId());
        unsubscribe.setCreatedAt(unsubscribeEntity.getCreatedAt().atOffset(ZoneOffset.UTC).toString());

        return unsubscribe;
    }

}
