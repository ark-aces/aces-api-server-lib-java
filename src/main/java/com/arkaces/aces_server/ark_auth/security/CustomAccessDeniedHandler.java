package com.arkaces.aces_server.ark_auth.security;

import com.arkaces.aces_server.common.error.ErrorCodes;
import com.arkaces.aces_server.common.error.GeneralError;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    
    private final ObjectMapper objectMapper;

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        log.info("Access denied error occurred: " + accessDeniedException.getMessage());

        GeneralError generalError = new GeneralError();
        generalError.setCode(ErrorCodes.BAD_CREDENTIALS_ERROR);
        generalError.setMessage("Authorization failed.");

        ErrorResponseWriter writer = new ErrorResponseWriter(objectMapper);
        writer.write(response, generalError);
    }
}
