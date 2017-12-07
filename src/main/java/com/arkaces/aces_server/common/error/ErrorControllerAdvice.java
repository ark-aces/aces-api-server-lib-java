package com.arkaces.aces_server.common.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public GeneralError handleNotFoundException(NotFoundException notFoundException) {
        log.warn("Not found exception thrown", notFoundException);

        GeneralError generalError = new GeneralError();
        generalError.setCode(notFoundException.getCode());
        generalError.setMessage(notFoundException.getMessage());

        return generalError;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ValidationError handleValidationError(ValidationException validationException) {
        log.warn("Validation exception thrown", validationException);

        ValidationError validationError = new ValidationError();
        validationError.setCode(validationException.getCode());
        validationError.setMessage(validationException.getMessage());
        validationError.setFieldErrors(validationException.getFieldErrors());

        return validationError;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public GeneralError handleException(Exception exception) {
        log.error("Uncaught exception thrown", exception);

        GeneralError generalError = new GeneralError();
        generalError.setCode(ErrorCodes.SERVER_ERROR);
        generalError.setMessage("An unexpected error has occurred.");

        return generalError;
    }

}
