package com.stayease.property_service.controller;

import com.stayease.property_service.dto.request.*;
import com.stayease.property_service.dto.response.PendingPropertyResponse;
import com.stayease.property_service.dto.response.PropertyResponse;
import com.stayease.property_service.dto.response.PropertySearchResponse;
import com.stayease.property_service.entity.Property;
import com.stayease.property_service.entity.PropertyStatus;
import com.stayease.property_service.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.stayease.property_service.dto.response.ApiResponse;

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
    public ResponseEntity<ApiResponse<List<PropertyResponse>>> getPropertiesByOwner(@PathVariable Long ownerId){
        List<PropertyResponse> properties=propertyService.getPropertiesByOwner(ownerId);
        if(properties==null || properties.isEmpty()){
            log.info("No properties found for owner ID: {}", ownerId);
            return  ResponseEntity.ok(new ApiResponse<>("SUCCESS", "No properties found : "+ownerId,null));
        }
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Properties fetched", properties));
    }

    @GetMapping("/owner/{ownerId}/count")
    @Operation(summary = "Count Properties by Owner")
    @PreAuthorize("hasRole('OWNER') or hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Long>> countPropertiesByOwner(@PathVariable Long ownerId){
        Long count=propertyService.countPropertiesByOwner(ownerId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Count retrieved", count));
    }

    @PostMapping("/addproperty")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Create Property")
    public ResponseEntity<ApiResponse<PropertyResponse>> createProperty(@Valid @RequestBody PropertyRequest request) {
        PropertyResponse property=propertyService.createProperty(request);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Property created successfully", property));
    }

    @GetMapping("/{propertyId}")
    @PreAuthorize("hasRole('USER') or hasRole('OWNER')")
    @Operation(summary = "Get Property By ID")
    public ResponseEntity<ApiResponse<PropertyResponse>> getPropertyById(@PathVariable Long propertyId){
        PropertyResponse property=propertyService.getPropertyById(propertyId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Property fetched successfully", property));
    }

    @PutMapping("/updateproperty/{propertyId}")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Update Property")
    public ResponseEntity<ApiResponse<PropertyResponse>> updateProperty(@Valid @PathVariable Long propertyId,@RequestBody UpdatePropertyRequest request){
        PropertyResponse property=propertyService.updateProperty(propertyId,request);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Property updated successfully", property));
    }

    @PreAuthorize("hasRole('OWNER') or hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/searchproperties")
    @Operation(summary = "Search Properties")
    public ResponseEntity<Page<PropertySearchResponse>> searchProperties(@RequestBody PropertySearchRequest request){
        Page<PropertySearchResponse> response=propertyService.searchProperties(request);
        return ResponseEntity.ok(response);
    }

    //Admin Operation Related to Properties
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/pending")
    @Operation(summary = "Pending Properties List")
    public ResponseEntity<List<PendingPropertyResponse>> getPendingProperties(){
        return ResponseEntity.ok(propertyService.getPendingProperties());
    }

    @PutMapping("/admin/approve/{propertyId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Approve Properties")
    public ResponseEntity<PropertyResponse> approveProperty(@PathVariable Long propertyId,@Valid @RequestBody ApprovePropertyRequest request) {
        PropertyResponse property = propertyService.approveProperty(propertyId,request);
        return ResponseEntity.ok(property);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/approved")
    @Operation(summary = "Approved Properties List")
    public ResponseEntity<List<PendingPropertyResponse>> approvedProperties(){
        return ResponseEntity.ok(propertyService.getApprovedProperties());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/reject/{propertyId}")
    @Operation(summary = "Reject Properties")
    public ResponseEntity<PropertyResponse> rejectProperty(@PathVariable Long propertyId,@Valid @RequestBody RejectPropertyRequest request){
        return ResponseEntity.ok(propertyService.rejectProperty(propertyId,request));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/rejected")
    @Operation(summary = "Rejected Properties List")
    public ResponseEntity<List<PendingPropertyResponse>> rejectedProperties(){
        return ResponseEntity.ok(propertyService.getRejectedProperties());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    @PutMapping("/activate/{propertyId}")
    @Operation(summary = "Activate Property")
    public ResponseEntity<ApiResponse<PropertyResponse>> activateProperty(@PathVariable Long propertyId){
        PropertyResponse property=propertyService.activateProperty(propertyId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS","Property activated successfully",property));
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    @PutMapping("/deactivate/{propertyId}")
    @Operation(summary = "DeActivate Property")
    public ResponseEntity<ApiResponse<PropertyResponse>> deActivateProperty(@PathVariable Long propertyId){
        PropertyResponse property=propertyService.deactivateProperty(propertyId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS","Property deactivated successfully",property));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    @DeleteMapping("/deleteproperty/{propertyId}")
    @Operation(summary = "Delete Property")
    public ResponseEntity<ApiResponse<PropertyResponse>> deleteProperty(@PathVariable Long propertyId){
        PropertyResponse response = propertyService.deleteProperty(propertyId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Property deleted successfully", response));
    }

}