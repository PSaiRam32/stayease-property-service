package com.stayease.property_service.service;

import com.stayease.property_service.dto.PropertyRequestDTO;
import com.stayease.property_service.dto.PropertyResponseDTO;


import java.util.List;

public interface PropertyService {
    PropertyResponseDTO createProperty(PropertyRequestDTO request);
    PropertyResponseDTO getPropertyById(Long propertyId);
    PropertyResponseDTO updateProperty(Long propertyId, PropertyRequestDTO request);
    List<PropertyResponseDTO> getPropertiesByOwner(Long OwnerId);
    Long countPropertiesByOwner(Long ownerId);
}