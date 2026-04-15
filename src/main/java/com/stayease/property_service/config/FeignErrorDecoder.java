package com.stayease.property_service.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();
        log.warn("Feign client error detected - Method: {}, Status code: {}", methodKey, status);
        return switch (status) {
            case 400 -> {
                log.error("Bad Request from downstream service - Method: {}", methodKey);
                yield new RuntimeException("Bad Request from downstream service");
            }
            case 404 -> {
                log.error("Resource not found in downstream service - Method: {}", methodKey);
                yield new RuntimeException("Resource not found in downstream service");
            }
            case 500 -> {
                log.error("Internal server error in downstream service - Method: {}", methodKey);
                yield new RuntimeException("Internal server error in downstream service");
            }
            default -> {
                log.error("Feign client error: {} - Method: {}", status, methodKey);
                yield new RuntimeException("Feign client error: " + status);
            }
        };
    }
}