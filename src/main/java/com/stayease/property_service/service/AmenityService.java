package com.stayease.property_service.service;

import com.stayease.property_service.dto.request.AmenityRequest;
import com.stayease.property_service.dto.response.AmenityResponse;
import java.util.List;


public interface AmenityService{
    AmenityResponse createAmenity(AmenityRequest request);
    List<AmenityResponse> getAllAmenities();
    AmenityResponse getAmenityById(Long amenityId);
    void linkAmenitiesToProperty(Long propertyId, List<Long> amenityIds);
}


