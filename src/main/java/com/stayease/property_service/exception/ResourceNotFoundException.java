package com.stayease.property_service.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
        log.error("ResourceNotFoundException created: {}", message);
    }
}
