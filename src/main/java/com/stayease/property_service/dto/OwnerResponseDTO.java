package com.stayease.property_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OwnerResponseDTO {
    private Long ownerId;
    private String name;
    private String email;
    private String phoneNumber;
    private String kycStatus;
}