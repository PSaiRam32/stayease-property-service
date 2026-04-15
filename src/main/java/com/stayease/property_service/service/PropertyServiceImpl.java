package com.stayease.property_service.service;

import com.stayease.property_service.config.OwnerClient;
import com.stayease.property_service.dto.OwnerResponseDTO;
import com.stayease.property_service.dto.PropertyRequestDTO;
import com.stayease.property_service.dto.PropertyResponseDTO;
import com.stayease.property_service.dto.RoomResponse;
import com.stayease.property_service.dto.AmenityResponseDTO;
import com.stayease.property_service.entity.Property;
import com.stayease.property_service.exception.BusinessException;
import com.stayease.property_service.repository.PropertyRepository;
import com.stayease.property_service.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final OwnerClient ownerClient;
    private final RoomRepository roomRepository;

    @Override
    public PropertyResponseDTO createProperty(PropertyRequestDTO request) {
        log.info("Creating property for owner ID: {}", request.getOwnerId());
        log.debug("Property details: title={}, location={}, description={}", request.getTitle(), request.getLocation(), request.getDescription());
        OwnerResponseDTO owner = ownerClient.getOwnerById(request.getOwnerId());
        if (owner == null) {
            log.error("Owner not found with ID: {}", request.getOwnerId());
            throw new BusinessException("Owner not found");
        }
        log.debug("Owner found: {}", owner);
        String kycStatus = ownerClient.getKycStatus(request.getOwnerId());
        log.debug("KYC status for owner ID {}: {}", request.getOwnerId(), kycStatus);
        if (!"VERIFIED".equals(kycStatus)) {
            log.error("Owner KYC not verified for owner ID: {}", request.getOwnerId());
            throw new BusinessException("Owner KYC not verified");
        }
        Property property = Property.builder()
                .ownerId(request.getOwnerId())
                .title(request.getTitle())
                .location(request.getLocation())
                .description(request.getDescription())
                .rating(request.getRating())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        property = propertyRepository.save(property);
        log.info("Property created successfully with ID: {}", property.getPropertyId());
        return mapToDTO(property);
    }

    @Override
    public PropertyResponseDTO getPropertyById(Long id) {
        log.debug("Fetching property with ID: {}", id);
        Property property = propertyRepository.findByPropertyId(id)
                .orElseThrow(() -> {
                    log.error("Property not found with ID: {}", id);
                    return new BusinessException("Property not found");
                });
        log.info("Property fetched successfully with ID: {}", id);
        return mapToDTO(property);
    }

    @Override
    public PropertyResponseDTO updateProperty(Long id, PropertyRequestDTO request) {
        log.info("Updating property with ID: {}", id);
        log.debug("Update request: title={}, location={}, description={}", request.getTitle(), request.getLocation(), request.getDescription());
        Property property = propertyRepository.findByPropertyId(id)
                .orElseThrow(() -> {
                    log.error("Property not found with ID to update the details: {}", id);
                    return new BusinessException("Property not found");
                });
        OwnerResponseDTO owner = ownerClient.getOwnerById(request.getOwnerId());
        if (owner == null) {
            log.error("Owner not found with ID to update the property: {}", request.getOwnerId());
            throw new BusinessException("Owner not found");
        }
        log.debug("Owner found to update the property: {}", owner);
        property.setTitle(request.getTitle());
        property.setLocation(request.getLocation());
        property.setDescription(request.getDescription());
        property.setUpdatedAt(LocalDateTime.now());
        Property updated = propertyRepository.save(property);
        log.info("Property updated successfully with ID: {}", id);
        return mapToDTO(updated);
    }

    @Override
    public List<PropertyResponseDTO> getPropertiesByOwner(Long ownerId) {
        log.debug("Fetching all properties for owner ID: {}", ownerId);
        List<PropertyResponseDTO> properties = propertyRepository.findByOwnerId(ownerId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        log.info("Found {} properties for owner ID: {}", properties.size(), ownerId);
        return properties;
    }

    @Override
    public Long countPropertiesByOwner(Long ownerId) {
        log.debug("Counting properties for owner ID: {}", ownerId);
        Long count = propertyRepository.countByOwnerId(ownerId);
        log.info("Property count for owner ID {}: {}", ownerId, count);
        return count;
    }

    private PropertyResponseDTO mapToDTO(Property property) {
        log.debug("Mapping property entity to DTO for property ID: {}", property.getPropertyId());
        List<com.stayease.property_service.entity.Room> rooms = roomRepository.findByProperty_PropertyId(property.getPropertyId());
        List<RoomResponse> roomResponses = rooms.stream().map(r -> RoomResponse.builder()
                .id(r.getId())
                .propertyId(r.getProperty().getPropertyId())
                .capacity(r.getCapacity())
                .price(r.getPrice())
                .availableCount(r.getAvailableCount())
                .build()).collect(Collectors.toList());
        java.util.Set<com.stayease.property_service.entity.Amenity> amenitySet = property.getAmenities();
        List<AmenityResponseDTO> amenities = (amenitySet == null) ? java.util.Collections.emptyList() : amenitySet.stream()
                .map(a -> AmenityResponseDTO.builder().id(a.getId()).name(a.getName()).build())
                .collect(Collectors.toList());
        return PropertyResponseDTO.builder()
                .id(property.getPropertyId())
                .ownerId(property.getOwnerId())
                .title(property.getTitle())
                .location(property.getLocation())
                .description(property.getDescription())
                .rating(property.getRating())
                .rooms(roomResponses)
                .amenities(amenities)
                .build();
    }
}