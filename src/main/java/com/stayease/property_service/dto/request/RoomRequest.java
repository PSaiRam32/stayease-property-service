package com.stayease.property_service.dto.request;

import com.stayease.property_service.entity.WashroomType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomRequest {
    @Column(nullable = false)
    private String roomNumber;
    @Min(1)
    private Integer sharingCapacity;
    @Min(1000)
    private Double price;
    private Integer availableCount;
    @Enumerated(EnumType.STRING)
    private WashroomType washroomType;
}