package com.stayease.property_service.dto.response;

import com.stayease.property_service.entity.PropertyStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyResponse{
    private Long propertyId;
    private Long ownerId;
    private String ownerName;
    private String phoneNumber;
    private String propertyTitle;
    private String description;
    private String location;
    private String city;
    private String state;
    private String country;
//    private Double latitude;
//    private Double longitude;
//    private String thumbnailUrl;
    private Double averageRating;
    private PropertyStatus status;
    private Boolean active;
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RoomResponse> rooms;
    private List<AmenityResponse> amenities;
}