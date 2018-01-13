package com.arkaces.aces_server.aces_listener.event;

import com.arkaces.aces_server.aces_listener.subscription.SubscriptionEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventPayloadMapper {

    private final ObjectMapper objectMapper;

    public EventPayload map(SubscriptionEntity subscriptionEntity, EventEntity eventEntity) {
        JsonNode data;
        try {
            data = objectMapper.readTree(eventEntity.getData());
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse event data", e);
        }

        EventPayload eventPayload = new EventPayload();
        eventPayload.setId(eventEntity.getId());
        eventPayload.setTransactionId(eventEntity.getTransactionId());
        eventPayload.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC).toString());
        eventPayload.setData(data);
        
        eventPayload.setSubscriptionId(subscriptionEntity.getId());

        return eventPayload;
    }
}
