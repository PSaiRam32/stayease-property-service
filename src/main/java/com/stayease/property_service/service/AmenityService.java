package com.stayease.property_service.service;

import com.stayease.property_service.dto.AmenityResponseDTO;
import com.stayease.property_service.entity.Amenity;

import java.util.List;

public interface AmenityService {
    AmenityResponseDTO createAmenity(Amenity amenity);
    List<AmenityResponseDTO> getAllAmenities();
    AmenityResponseDTO getAmenityById(Long amenityId);
    void linkAmenitiesToProperty(Long propertyId, List<Long> amenityIds);
}


