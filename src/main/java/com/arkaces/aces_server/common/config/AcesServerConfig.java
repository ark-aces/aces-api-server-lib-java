package com.arkaces.aces_server.common.config;

import com.arkaces.aces_server.common.identifer.IdentifierGenerator;
import com.arkaces.aces_server.common.json.NiceObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AcesServerConfig {

    @Bean
    public RestTemplate callbackRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        builder.indentOutput(true);
        return builder;
    }

    @Bean
    public IdentifierGenerator identifierGenerator() {
        return new IdentifierGenerator();
    }

    @Bean
    public NiceObjectMapper logObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        return new NiceObjectMapper(objectMapper);
    }

    @Bean
    public NiceObjectMapper dtoObjectMapper() {
        return new NiceObjectMapper(new ObjectMapper());
    }

}
