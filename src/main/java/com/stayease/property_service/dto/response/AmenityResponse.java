package com.stayease.property_service.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AmenityResponse{
    private Long amenityId;
    private String name;
}

