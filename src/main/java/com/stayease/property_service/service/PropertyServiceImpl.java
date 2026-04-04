package com.stayease.property_service.service;

import com.stayease.property_service.dto.PropertyRequestDTO;
import com.stayease.property_service.dto.PropertyResponseDTO;
import com.stayease.property_service.dto.RoomRequestDTO;
import com.stayease.property_service.entity.Property;
import com.stayease.property_service.entity.Room;
import com.stayease.property_service.exception.BusinessException;
import com.stayease.property_service.repository.PropertyRepository;
import com.stayease.property_service.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final RoomRepository roomRepository;

    @Override
    public PropertyResponseDTO createProperty(PropertyRequestDTO request) {
        String userId = getLoggedInUser();
        Property property = Property.builder()
                .ownerId(userId)
                .title(request.getTitle())
                .location(request.getLocation())
                .description(request.getDescription())
                .build();

        return mapToDTO(propertyRepository.save(property));
    }

    @Override
    public PropertyResponseDTO getProperty(Long id) {
        Property property = propertyRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new BusinessException("Property not found"));
        return mapToDTO(property);
    }

    @Override
    public PropertyResponseDTO updateProperty(Long id, PropertyRequestDTO request) {
        Property property = propertyRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new BusinessException("Property not found"));
        validateOwnership(property);
        property.setTitle(request.getTitle());
        property.setLocation(request.getLocation());
        property.setDescription(request.getDescription());
        return mapToDTO(propertyRepository.save(property));
    }

    @Override
    public List<PropertyResponseDTO> getPropertiesByOwner() {
        String userId = getLoggedInUser();
        return propertyRepository.findByOwnerIdAndIsActiveTrue(userId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public void addRoom(Long propertyId, RoomRequestDTO request) {
        Property property = propertyRepository.findByIdAndIsActiveTrue(propertyId)
                .orElseThrow(() -> new BusinessException("Property not found"));
        validateOwnership(property);
        Room room = Room.builder()
                .capacity(request.getCapacity())
                .price(request.getPrice())
                .available(true)
                .property(property)
                .build();
        roomRepository.save(room);
    }

    @Override
    public List<Room> getRooms(Long propertyId) {
        return roomRepository.findByPropertyId(propertyId);
    }

    private String getLoggedInUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private void validateOwnership(Property property) {
        if (!property.getOwnerId().equals(getLoggedInUser())) {
            throw new AccessDeniedException("Unauthorized");
        }
    }

    private PropertyResponseDTO mapToDTO(Property property) {
        return PropertyResponseDTO.builder()
                .id(property.getId())
                .ownerId(property.getOwnerId())
                .title(property.getTitle())
                .location(property.getLocation())
                .description(property.getDescription())
                .build();
    }
}