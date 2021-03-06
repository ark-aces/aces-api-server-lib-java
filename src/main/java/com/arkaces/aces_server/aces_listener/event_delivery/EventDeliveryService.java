package com.arkaces.aces_server.aces_listener.event_delivery;

import com.arkaces.aces_server.aces_listener.event.EventEntity;
import com.arkaces.aces_server.aces_listener.event.EventPayload;
import com.arkaces.aces_server.aces_listener.event.EventPayloadMapper;
import com.arkaces.aces_server.aces_listener.event.EventRepository;
import com.arkaces.aces_server.aces_listener.subscription.SubscriptionEntity;
import com.arkaces.aces_server.aces_listener.subscription.SubscriptionRepository;
import com.arkaces.aces_server.aces_listener.subscription.SubscriptionStatus;
import com.arkaces.aces_server.aces_listener.subscription_event.SubscriptionEventEntity;
import com.arkaces.aces_server.aces_listener.subscription_event.SubscriptionEventRepository;
import com.arkaces.aces_server.aces_listener.subscription_event.SubscriptionEventStatus;
import com.arkaces.aces_server.common.identifer.IdentifierGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional
public class EventDeliveryService {

    private final IdentifierGenerator identifierGenerator;
    private final RestTemplate callbackRestTemplate;
    private final EventRepository eventRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionEventRepository subscriptionEventRepository;
    private final EventPayloadMapper eventPayloadMapper;

    public void saveSubscriptionEvents(String transactionId, String recipientAddress, Integer confirmations, JsonNode data) {
        List<SubscriptionEntity> subscriptionEntities = subscriptionRepository.findAllByStatus(SubscriptionStatus.ACTIVE);
        for (SubscriptionEntity subscriptionEntity : subscriptionEntities) {
            boolean matchesRecipientAddress = subscriptionEntity.getRecipientAddress() == null
                    || subscriptionEntity.getRecipientAddress().equals(recipientAddress);
            boolean matchesMinConfirmations = confirmations >= subscriptionEntity.getMinConfirmations();
            if (matchesRecipientAddress && matchesMinConfirmations) {
                saveSubscriptionEvent(subscriptionEntity, transactionId, data);
            }
        }
    }

    public void saveSubscriptionEvent(
            SubscriptionEntity subscriptionEntity,
            String transactionId,
            JsonNode data
    ) {
        Long subscriptionEntityPid = subscriptionEntity.getPid();

        SubscriptionEventEntity existingSubscriptionEventEntity
                = subscriptionEventRepository.findOne(subscriptionEntityPid, transactionId);
        if (existingSubscriptionEventEntity == null) {
            log.info("Creating new subscription event for subscription " + subscriptionEntity.getId() +
                " and transaction id " + transactionId);

            LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

            EventEntity eventEntity = eventRepository.findOneByTransactionId(transactionId);
            if (eventEntity == null) {
                eventEntity = new EventEntity();
                eventEntity.setId(identifierGenerator.generate());
                eventEntity.setCreatedAt(now);
                eventEntity.setTransactionId(transactionId);
                eventEntity.setData(data.toString());
                eventRepository.save(eventEntity);
            }

            SubscriptionEventEntity subscriptionEventEntity = new SubscriptionEventEntity();
            subscriptionEventEntity.setCreatedAt(now);
            subscriptionEventEntity.setSubscriptionEntity(subscriptionEntity);
            subscriptionEventEntity.setEventEntity(eventEntity);
            subscriptionEventEntity.setStatus(SubscriptionEventStatus.NEW);
            subscriptionEventEntity.setTries(0);
            subscriptionEventRepository.save(subscriptionEventEntity);

            log.info("Saved subscription event entity " + subscriptionEventEntity.getPid());
        }
    }

    public void trySendEvent(SubscriptionEventEntity subscriptionEventEntity) {
        SubscriptionEntity subscriptionEntity = subscriptionEventEntity.getSubscriptionEntity();

        EventEntity eventEntity = subscriptionEventEntity.getEventEntity();

        if (subscriptionEventEntity.getTries() > 5) {
            log.info("Subscription event " + subscriptionEntity.getId() + " tried too many times - setting to FAILED");
            subscriptionEventEntity.setStatus(SubscriptionEventStatus.FAILED);

            // todo: might want to cancel subscriptions less aggressively
            // Here we cancel a subscription if it fails more than 5 times 1 second apart
            subscriptionEntity.setStatus(SubscriptionStatus.CANCELLED);
            subscriptionRepository.save(subscriptionEntity);
        } else {
            subscriptionEventEntity.setTries(subscriptionEventEntity.getTries() + 1);

            log.info("Trying to send subscription event " + subscriptionEventEntity.getPid() + " (try "
                    + subscriptionEventEntity.getTries() + ")");

            EventPayload eventPayload = eventPayloadMapper.map(subscriptionEntity, eventEntity);
            String callbackUrl = subscriptionEntity.getCallbackUrl();
            try {
                ResponseEntity<String> response = callbackRestTemplate.postForEntity(callbackUrl, eventPayload, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    log.info("Delivered event " + eventPayload.getId() + " to subscriber " + subscriptionEntity.getId());
                    subscriptionEventEntity.setStatus(SubscriptionEventStatus.DELIVERED);
                    subscriptionEventRepository.save(subscriptionEventEntity);
                } else {
                    log.info("Subscription event post returned non-200 response code and will retry later");
                }
            } catch (RestClientResponseException e) {
                log.warn("Failed to post event to subscriber: " + e.getResponseBodyAsString());
            } catch (Exception e) {
                log.warn("Failed to post event to subscriber", e);
            }
        }

        subscriptionEventRepository.save(subscriptionEventEntity);
    }

}
