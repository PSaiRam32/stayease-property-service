package com.stayease.property_service.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
public class RoomRequestDTO {
    @Min(1)
    private Integer capacity;
    @Min(1000)
    private Double price;
}