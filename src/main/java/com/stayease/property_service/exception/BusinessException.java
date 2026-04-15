package com.stayease.property_service.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
        log.error("BusinessException created: {}", message);
    }
}