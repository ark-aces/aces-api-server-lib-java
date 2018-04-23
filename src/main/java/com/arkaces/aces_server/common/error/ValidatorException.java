package com.arkaces.aces_server.common.error;

import groovy.transform.EqualsAndHashCode;
import lombok.Data;
import org.springframework.validation.BindingResult;

@Data
@EqualsAndHashCode(callSuper = false)
public class ValidatorException extends RuntimeException {

    private BindingResult bindingResult;

    public ValidatorException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}
