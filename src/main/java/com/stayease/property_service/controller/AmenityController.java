package com.stayease.property_service.controller;

import com.stayease.property_service.dto.AmenityResponseDTO;
import com.stayease.property_service.dto.AmenityRequestDTO;
import com.stayease.property_service.entity.Amenity;
import com.stayease.property_service.dto.ApiResponse;
import com.stayease.property_service.service.AmenityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/amenities")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Amenity Management", description = "APIs for managing Amenities")
public class AmenityController {

    private final AmenityService amenityService;

    @PostMapping("/createamenity")
    @Operation(summary = "Create Amenity")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<AmenityResponseDTO>> createAmenity(@Valid @RequestBody AmenityRequestDTO request) {
        log.info("Creating amenity: {}", request.getName());
        Amenity amenity = Amenity.builder().name(request.getName()).build();
        AmenityResponseDTO dto = amenityService.createAmenity(amenity);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Amenity created", dto));
    }

    @GetMapping("/getallamenities")
    @Operation(summary = "List Amenities")
    @PreAuthorize("hasRole('OWNER') or hasRole('USER')")
    public ResponseEntity<ApiResponse<List<AmenityResponseDTO>>> listAmenities() {
        List<AmenityResponseDTO> list = amenityService.getAllAmenities();
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Amenities fetched", list));
    }

    @GetMapping("/getamenity/{amenityId}")
    @Operation(summary = "Get Amenity")
    @PreAuthorize("hasRole('OWNER') or hasRole('USER')")
    public ResponseEntity<ApiResponse<AmenityResponseDTO>> getAmenityById(@PathVariable Long amenityId) {
        AmenityResponseDTO dto = amenityService.getAmenityById(amenityId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Amenity fetched", dto));
    }

    @PostMapping("/link/{propertyId}")
    @Operation(summary = "Link amenities to property")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<String>> linkAmenitiesToProperty(@PathVariable Long propertyId, @RequestBody List<Long> amenityIds) {
        log.info("Linking {} amenities to property ID: {}", amenityIds.size(), propertyId);
        // delegate to amenityService (we'll implement linking there)
        amenityService.linkAmenitiesToProperty(propertyId, amenityIds);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Amenities linked", null));
    }
}


