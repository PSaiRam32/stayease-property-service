package com.stayease.property_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyRequest{
    @NotNull(message = "Owner ID is required.")
    private Long ownerId;
    @NotBlank(message = "Property title is required.")
    @Size(max = 150)
    private String propertyTitle;
    @NotBlank(message = "Description is required.")
    @Size(max = 1000)
    private String description;
    @NotBlank(message = "Location is required.")
    private String location;
    @NotBlank(message = "City is required.")
    private String city;
    @NotBlank(message = "State is required.")
    private String state;
    @NotBlank(message = "Country is required.")
    private String country;
//    @DecimalMin(value = "-90.0")
//    @DecimalMax(value = "90.0")
//    private Double latitude;
//    @DecimalMin(value = "-180.0")
//    @DecimalMax(value = "180.0")
//    private Double longitude;
//    private String thumbnailUrl;
}