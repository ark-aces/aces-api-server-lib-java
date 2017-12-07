package com.arkaces.aces_server.aces_listener.event;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class Event {
    private String id;
    private String transactionId;
    private JsonNode data;
    private String createdAt;
}
