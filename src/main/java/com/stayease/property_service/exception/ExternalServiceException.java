package com.stayease.property_service.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExternalServiceException extends RuntimeException{
    public ExternalServiceException(String message){
        super(message);
        log.error("ExternalServiceException created: {}",message);
    }
}
