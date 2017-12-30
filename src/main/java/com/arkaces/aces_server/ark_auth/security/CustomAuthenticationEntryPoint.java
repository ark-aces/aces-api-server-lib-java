package com.arkaces.aces_server.ark_auth.security;

import com.arkaces.aces_server.common.error.ErrorCodes;
import com.arkaces.aces_server.common.error.GeneralError;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException, ServletException {
        log.info("Authentication failed: {}", authException.getMessage());

        response.setContentType("application/json");

        GeneralError generalError = new GeneralError();

        if (authException instanceof BadCredentialsException) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            generalError.setCode(ErrorCodes.BAD_CREDENTIALS_ERROR);
            generalError.setMessage("Credentials given are invalid.");
        }
        else if (authException instanceof InactiveApiKeyException) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            generalError.setCode(ErrorCodes.INACTIVE_API_KEY);
            generalError.setMessage("Account API Key is not ACTIVE. Ensure user Ark address " +
                "has sufficient ark stake and payment Ark address has sufficient funds to cover activation fee.");
        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            generalError.setCode(ErrorCodes.AUTHORIZATION_REQUIRED_ERROR);
            generalError.setMessage("HTTP Basic Authorization required.");
        }
        

        ErrorResponseWriter writer = new ErrorResponseWriter(objectMapper);
        writer.write(response, generalError);
    }
}