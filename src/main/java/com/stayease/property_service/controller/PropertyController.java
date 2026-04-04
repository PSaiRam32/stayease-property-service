package com.stayease.property_service.controller;

import com.stayease.property_service.dto.*;
import com.stayease.property_service.entity.Room;
import com.stayease.property_service.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.stayease.property_service.dto.ApiResponse;

@RestController
@RequestMapping("/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<ApiResponse<PropertyResponseDTO>> createProperty(@Valid @RequestBody PropertyRequestDTO request) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Property created",
                        propertyService.createProperty(request))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PropertyResponseDTO>> getProperty(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Property fetched",
                        propertyService.getProperty(id))
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<ApiResponse<PropertyResponseDTO>> updateProperty(@PathVariable Long id, @RequestBody PropertyRequestDTO request) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Updated",
                        propertyService.updateProperty(id, request))
        );
    }

    @GetMapping("/owner")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<ApiResponse<List<PropertyResponseDTO>>> getOwnerProperties() {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Owner properties",
                        propertyService.getPropertiesByOwner())
        );
    }

    @PostMapping("/{id}/rooms")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<ApiResponse<String>> addRoom(@PathVariable Long id, @RequestBody RoomRequestDTO request) {
        propertyService.addRoom(id, request);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Room added", null)
        );
    }

    @GetMapping("/{id}/rooms")
    public ResponseEntity<ApiResponse<List<Room>>> getRooms(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Rooms fetched",
                        propertyService.getRooms(id))
        );
    }
}