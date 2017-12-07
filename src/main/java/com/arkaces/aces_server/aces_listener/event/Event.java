package com.arkaces.aces_server.aces_listener.event;

import lombok.Data;

@Data
public class Event {
    private String id;
    private String transactionId;
    private String data;
    private String createdAt;
}
