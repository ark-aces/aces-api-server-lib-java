package com.arkaces.aces_api_server_lib.error;

import lombok.Data;

@Data
public class GeneralError {
    private String code;
    private String message;
}
