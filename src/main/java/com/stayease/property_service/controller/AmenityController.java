package com.stayease.property_service.controller;

import com.stayease.property_service.dto.response.AmenityResponse;
import com.stayease.property_service.dto.request.AmenityRequest;
import com.stayease.property_service.entity.Amenity;
import com.stayease.property_service.dto.response.ApiResponse;
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
public class AmenityController{

    private final AmenityService amenityService;

    @PostMapping("/createamenity")
    @Operation(summary = "Create Amenity")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<AmenityResponse>> createAmenity(@Valid @RequestBody AmenityRequest request){
        Amenity amenity=Amenity.builder().name(request.getName()).build();
        AmenityResponse response=amenityService.createAmenity(request);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Amenity created", response));
    }

    @GetMapping("/getallamenities")
    @Operation(summary = "List Amenities")
    @PreAuthorize("hasRole('OWNER') or hasRole('USER')")
    public ResponseEntity<ApiResponse<List<AmenityResponse>>> listAmenities(){
        log.info("Fetching all amenities.");
        List<AmenityResponse> amenitieslist=amenityService.getAllAmenities();
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Amenities retrieved successfully.",amenitieslist));
    }

    @GetMapping("/getamenity/{amenityId}")
    @Operation(summary = "Get Amenity")
    @PreAuthorize("hasRole('OWNER') or hasRole('USER')")
    public ResponseEntity<ApiResponse<AmenityResponse>> getAmenityById(@PathVariable Long amenityId){
        AmenityResponse amenity=amenityService.getAmenityById(amenityId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Amenity retrieved successfully.", amenity));
    }

    @PostMapping("/link/{propertyId}")
    @Operation(summary = "Link amenities to property")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<String>> linkAmenitiesToProperty(@PathVariable Long propertyId, @RequestBody List<Long> amenityIds){
        // delegate to amenityService (we'll implement linking there)
        amenityService.linkAmenitiesToProperty(propertyId, amenityIds);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Amenities linked successfully.",  "Amenities linked successfully."));
    }
}


