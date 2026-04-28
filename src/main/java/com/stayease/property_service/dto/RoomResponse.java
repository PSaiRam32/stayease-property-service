package com.stayease.property_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomResponse {
    private Long roomId;
    private Long propertyId;
    private Long OwnerId;
    private String ownerName;
    private String phoneNumber;
    private String propertyName;
    private Integer capacity;
    private Double price;
    private Integer availableCount;
}