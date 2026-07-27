package com.stayease.property_service.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertySummaryResponse{
    private Long propertyId;
    private String propertyName;
}