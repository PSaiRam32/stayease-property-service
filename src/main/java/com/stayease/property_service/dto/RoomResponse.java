package com.stayease.property_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomResponse {
    private Long id;
    private Long propertyId;
    private Integer capacity;
    private Double price;
    private Integer availableCount;
}