package com.arkaces.aces_server.aces_service.contract;

import lombok.Data;

@Data
public class CreateContractRequest<T> {
    private String correlationId;
    private T arguments;
}
