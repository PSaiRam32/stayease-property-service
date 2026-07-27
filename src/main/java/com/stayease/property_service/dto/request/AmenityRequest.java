package com.stayease.property_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmenityRequest{
    @NotBlank(message = "Amenity name is required.")
    private String name;
}

