package com.arkaces.aces_api_server_lib;

import lombok.Data;

import java.util.List;

@Data
public class ValidationError {

    private String code;
    private String message;
    private List<FieldError> fieldErrors;
}
