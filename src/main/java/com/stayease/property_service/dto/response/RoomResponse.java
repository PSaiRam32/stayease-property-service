package com.stayease.property_service.dto.response;

import com.stayease.property_service.entity.WashroomType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {
    private Long roomId;
    private String roomNumber;
    private Long propertyId;
    private Long OwnerId;
    private String ownerName;
    private String phoneNumber;
    private String propertyName;
    private Integer sharingCapacity;
    private Double price;
    private Integer availableCount;
    @Enumerated(EnumType.STRING)
    private WashroomType washroomType;
}