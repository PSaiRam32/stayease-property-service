package com.stayease.property_service.controller;

import com.stayease.property_service.dto.*;
import com.stayease.property_service.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.stayease.property_service.dto.ApiResponse;

@RestController
@RequestMapping("/properties")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Property Management", description = "APIs for managing Properties")
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Get Properties By Owner")
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PropertyResponseDTO>>> getPropertiesByOwner(@PathVariable Long ownerId) {
        log.debug("Fetching properties for owner ID: {}", ownerId);
        List<PropertyResponseDTO> properties = propertyService.getPropertiesByOwner(ownerId);
        if(properties==null || properties.isEmpty()){
            log.info("No properties found for owner ID: {}", ownerId);
            return  ResponseEntity.ok(new ApiResponse<>("SUCCESS", "No properties found : "+ownerId,null));
        }
        log.info("Successfully fetched {} properties for owner ID: {}", properties.size(), ownerId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Properties fetched", properties));
    }

    @GetMapping("/owner/{ownerId}/count")
    @Operation(summary = "Count Properties by Owner")
    @PreAuthorize("hasRole('OWNER') or hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Long>> countPropertiesByOwner(@PathVariable Long ownerId) {
        log.debug("Counting properties for owner ID: {}", ownerId);
        Long count = propertyService.countPropertiesByOwner(ownerId);
        log.info("Owner ID: {} has {} properties", ownerId, count);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Count retrieved", count));
    }

    @PostMapping("/addproperty")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Create Property")
    public ResponseEntity<ApiResponse<PropertyResponseDTO>> createProperty(@Valid @RequestBody PropertyRequestDTO request) {
        log.info("Creating new property for owner ID: {}", request.getOwnerId());
        log.debug("Property request: title={}, location={}", request.getTitle(), request.getLocation());
        PropertyResponseDTO property = propertyService.createProperty(request);
        log.info("Property created successfully with ID: {}", property.getPropertyId());
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Property created successfully", property));
    }

    @GetMapping("/{propertyId}")
    @PreAuthorize("hasRole('USER') or hasRole('OWNER')")
    @Operation(summary = "Get Property By ID")
    public ResponseEntity<ApiResponse<PropertyResponseDTO>> getPropertyById(@PathVariable Long propertyId) {
        log.debug("Fetching property with ID: {}", propertyId);
        PropertyResponseDTO property = propertyService.getPropertyById(propertyId);
        log.info("Property fetched successfully with ID: {}", propertyId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Property fetched successfully", property));
    }

    @PutMapping("/{propertyId}")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Update Property")
    public ResponseEntity<ApiResponse<PropertyResponseDTO>> updateProperty(@PathVariable Long propertyId, @Valid @RequestBody PropertyRequestDTO request) {
        log.info("Updating property with ID: {}", propertyId);
        log.debug("Update request: title={}, location={}", request.getTitle(), request.getLocation());
        PropertyResponseDTO property = propertyService.updateProperty(propertyId, request);
        log.info("Property updated successfully with ID: {}", propertyId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Property updated successfully", property));
    }
}