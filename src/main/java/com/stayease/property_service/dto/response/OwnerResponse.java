package com.stayease.property_service.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OwnerResponse{
    private Long ownerId;
    private String name;
    private String email;
    private String phone;
    private String kycStatus;
}