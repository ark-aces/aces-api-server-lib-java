package com.arkaces.aces_server.ark_auth;

import ark_java_client.ArkClient;
import ark_java_client.ArkNetwork;
import ark_java_client.ArkNetworkFactory;
import ark_java_client.HttpArkClientFactory;
import com.arkaces.aces_server.ark_auth.security.WebSecurityConfig;
import com.arkaces.aces_server.common.api_key_generation.ApiKeyGenerator;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.math.BigDecimal;

@Configuration
@EnableJpaRepositories(basePackages = "com.arkaces.aces_server.ark_auth")
@EntityScan(basePackages = "com.arkaces.aces_server.ark_auth", basePackageClasses = Jsr310JpaConverters.class)
@ComponentScan(basePackages = "com.arkaces.aces_server.ark_auth")
@Import(WebSecurityConfig.class)
public class ArkAuthConfig {

    @Bean
    public ApiKeyGenerator apiKeyGenerator() {
        return new ApiKeyGenerator();
    }

    @Bean
    public ArkClient arkAuthArkClient(Environment environment) {
        ArkNetworkFactory arkNetworkFactory = new ArkNetworkFactory();
        String arkNetworkConfigPath = environment.getProperty("arkAuth.arkNetworkConfigPath");
        ArkNetwork arkNetwork = arkNetworkFactory.createFromYml(arkNetworkConfigPath);

        HttpArkClientFactory httpArkClientFactory = new HttpArkClientFactory();

        return httpArkClientFactory.create(arkNetwork);
    }

    @Bean
    public String arkAuthServiceArkAddress(Environment environment) {
        return environment.getProperty("arkAuth.serviceArkAddress");
    }

    @Bean
    public BigDecimal arkAuthMinArkStake(Environment environment) {
        return new BigDecimal(environment.getProperty("arkAuth.minArkStake", "0.00"));
    }

    @Bean
    public BigDecimal arkAuthArkFee(Environment environment) {
        return new BigDecimal(environment.getProperty("arkAuth.arkFee", "0.00"));
    }

}
