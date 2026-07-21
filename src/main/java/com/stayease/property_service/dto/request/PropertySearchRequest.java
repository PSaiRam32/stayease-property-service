package com.stayease.property_service.dto.request;

import com.stayease.property_service.entity.PropertyStatus;
import com.stayease.property_service.entity.WashroomType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertySearchRequest{
    private String city;
    private String state;
    private String country;
    @PositiveOrZero(message = "Minimum price cannot be negative.")
    private Double minPrice;
    @Positive(message = "Maximum price must be greater than zero.")
    private Double maxPrice;
    @Positive(message = "Sharing capacity must be greater than zero.")
    private Integer sharingCapacity;
    private WashroomType washroomType;
    private Set<Long> amenityIds;
    private PropertyStatus propertyStatus;
    // Pagination
    @Builder.Default
    @Min(value = 0, message = "Page number cannot be negative.")
    private Integer page = 0;
    @Builder.Default
    @Min(value = 1, message = "Page size must be at least 1.")
    @Max(value = 100, message = "Page size cannot exceed 100.")
    private Integer size = 10;
    // Sorting
    @Builder.Default
    private String sortBy = "averageRating";
    @Builder.Default
    private String sortDirection = "DESC";
}