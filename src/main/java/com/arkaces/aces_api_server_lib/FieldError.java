package com.arkaces.aces_api_server_lib;

import lombok.Data;

@Data
public class FieldError {

    private String field;
    private String code;
    private String message;

}
