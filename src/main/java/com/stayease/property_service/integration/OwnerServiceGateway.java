package com.stayease.property_service.integration;

import com.stayease.property_service.config.OwnerClient;
import com.stayease.property_service.dto.response.OwnerResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerServiceGateway {

    private final OwnerClient ownerClient;

    @Retry(name="owner-service")
    @CircuitBreaker(name="owner-service",fallbackMethod="getOwnerByIdFallback")
    public OwnerResponse getOwnerById(Long ownerId) {
        log.info("Calling Owner Service to fetch owner: {}", ownerId);
        return ownerClient.getOwnerById(ownerId);
    }

    public OwnerResponse getOwnerByIdFallback(Long ownerId, Exception ex){
        throw new RuntimeException(
                "Owner Service is currently unavailable while fetching ownerId: " + ownerId,ex);
    }

    @Retry(name="owner-service")
    @CircuitBreaker(name="owner-service",fallbackMethod="getKycStatusFallback")
    public String getKycStatus(Long ownerId) {
        log.info("Calling Owner Service to fetch KYC status for owner: {}", ownerId);
        return ownerClient.getKycStatus(ownerId);
    }

    public String getKycStatusFallback(Long ownerId, Exception ex){
        throw new RuntimeException(
                "Owner Service is currently unavailable while fetching KYC status for ownerId: " + ownerId,ex);
    }

    @Retry(name="owner-service")
    @CircuitBreaker(name="owner-service",fallbackMethod="getOwnersByIdsFallback")
    public List<OwnerResponse> getOwnersByIds(List<Long> ownerIds){
        log.info("Calling Owner Service to fetch owners.");
        return ownerClient.getOwnersByIds(ownerIds);
    }

    public List<OwnerResponse> getOwnersByIdsFallback(List<Long> ownerIds, Exception ex){
        log.error("Owner Service is unavailable while fetching {} owners.",ownerIds!=null?ownerIds.size():0,ex);
        throw new RuntimeException(
                "Owner Service is currently unavailable while fetching owners for the requested ownerIds.",ex);
    }
}