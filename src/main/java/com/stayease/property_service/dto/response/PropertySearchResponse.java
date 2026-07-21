package com.stayease.property_service.dto.response;

import com.stayease.property_service.entity.PropertyStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertySearchResponse{
    private Long propertyId;
    private String propertyTitle;
    private String description;
    private String city;
    private String state;
    private String country;
//     Lowest room price available in this property. Displayed in search results.
    private Double startingPrice;
    private Double averageRating;
//    private String thumbnailUrl;
    private PropertyStatus status;
    private List<String> amenities;
}