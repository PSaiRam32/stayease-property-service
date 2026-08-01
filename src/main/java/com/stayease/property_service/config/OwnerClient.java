package com.stayease.property_service.config;


import com.stayease.property_service.dto.response.OwnerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(name = "owner-service",configuration = FeignConfig.class)
public interface OwnerClient{

    @GetMapping("/owners/owners-internal/{ownerId}")
    OwnerResponse getOwnerById(@PathVariable("ownerId") Long ownerId);

    @GetMapping("/owners/owners-internal/kyc-status/{ownerId}")
    String getKycStatus(@PathVariable("ownerId") Long ownerId);

    @GetMapping("/owners/owners-interal/ownerslist")
    List<OwnerResponse> getOwnersByIds(@RequestBody  List<Long> ownerIds);
}
