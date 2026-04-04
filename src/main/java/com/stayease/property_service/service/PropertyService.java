package com.stayease.property_service.service;

import com.stayease.property_service.dto.PropertyRequestDTO;
import com.stayease.property_service.dto.PropertyResponseDTO;
import com.stayease.property_service.dto.RoomRequestDTO;
import com.stayease.property_service.entity.Room;

import java.util.List;

public interface PropertyService {
    PropertyResponseDTO createProperty(PropertyRequestDTO request);
    PropertyResponseDTO getProperty(Long id);
    PropertyResponseDTO updateProperty(Long id, PropertyRequestDTO request);
    List<PropertyResponseDTO> getPropertiesByOwner();
    void addRoom(Long propertyId, RoomRequestDTO request);
    List<Room> getRooms(Long propertyId);
}