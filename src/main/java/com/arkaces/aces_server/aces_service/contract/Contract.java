package com.arkaces.aces_server.aces_service.contract;

import lombok.Data;

@Data
public class Contract<T> {
    private String id;
    private String createdAt;
    private String expiresAt;
    private String correlationId;
    private String status;
    private T results;
}
