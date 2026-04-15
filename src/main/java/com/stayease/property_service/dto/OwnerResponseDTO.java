package com.stayease.property_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OwnerResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String kycStatus;
}