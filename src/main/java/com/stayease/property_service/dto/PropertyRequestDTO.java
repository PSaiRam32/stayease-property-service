package com.stayease.property_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class PropertyRequestDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String location;
    private String description;
    @NotNull
    private Long ownerId;
    private Double rating;
}