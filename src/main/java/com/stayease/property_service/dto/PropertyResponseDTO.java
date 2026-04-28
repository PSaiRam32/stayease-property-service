package com.stayease.property_service.dto;

import lombok.*;
import java.util.List;


@Getter
@Setter
@Builder
public class PropertyResponseDTO {
    public Long propertyId;
    public Long ownerId;
    public String ownerName;
    public String phoneNumber;
    public String title;
    public String location;
    public String description;
    public Double rating;
    private List<RoomResponse> rooms;
    private List<AmenityResponseDTO> amenities;
}