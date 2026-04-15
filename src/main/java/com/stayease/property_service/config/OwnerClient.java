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

    @GetMapping("/owners/owners-internal/{id}")
    OwnerResponseDTO getOwnerById(@PathVariable("id") Long id);

    @GetMapping("/owners/owners-internal/kyc-status/{id}")
    String getKycStatus(@PathVariable("id") Long id);
}
