package com.stayease.property_service.config;


import com.stayease.property_service.dto.OwnerResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(
        name = "owner-service",
        url = "${services.owner.url}",
        configuration = FeignClientConfig.class
)
public interface OwnerClient {

    @GetMapping("/owners/owners-internal/{ownerId}")
    OwnerResponseDTO getOwnerById(@PathVariable("ownerId") Long ownerId);

    @GetMapping("/owners/owners-internal/kyc-status/{ownerId}")
    String getKycStatus(@PathVariable("ownerId") Long ownerId);
}
