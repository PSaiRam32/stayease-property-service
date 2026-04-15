package com.stayease.property_service.config;

import feign.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FeignLoggingConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        log.info("Initializing Feign logger level to FULL");
        return Logger.Level.FULL;
    }
}