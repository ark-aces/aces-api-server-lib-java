package com.arkaces.aces_server.ark_auth.security;

import com.arkaces.aces_server.common.error.GeneralError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ErrorResponseWriter {

    private final ObjectMapper objectMapper;

    public ErrorResponseWriter(ObjectMapper objectMapper) {
       this.objectMapper = objectMapper;
    }

    public void write(HttpServletResponse response, GeneralError generalError) throws IOException {
        ObjectWriter jsonWriter = objectMapper.writer();
        String json = jsonWriter.writeValueAsString(generalError);

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
        out.close();
    }

}
