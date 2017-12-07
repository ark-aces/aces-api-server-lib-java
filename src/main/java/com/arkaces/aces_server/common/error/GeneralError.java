package com.arkaces.aces_server.common.error;

import lombok.Data;

@Data
public class GeneralError {
    private String code;
    private String message;
}
