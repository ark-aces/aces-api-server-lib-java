package com.arkaces.aces_server.aces_service.config;

import com.arkaces.aces_server.common.config.AcesServerConfig;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Import(AcesServerConfig.class)
@EnableJpaRepositories(basePackages = "com.arkaces.aces_server.aces_service")
@EntityScan(basePackages = "com.arkaces.aces_server.aces_service", basePackageClasses = Jsr310JpaConverters.class)
@ComponentScan(basePackages = {
    "com.arkaces.aces_server.common",
    "com.arkaces.aces_server.aces_service"
})
public class AcesServiceConfig {

}
