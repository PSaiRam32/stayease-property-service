package com.stayease.property_service.service;

import com.stayease.property_service.dto.request.*;
import com.stayease.property_service.dto.response.PendingPropertyResponse;
import com.stayease.property_service.dto.response.PropertyResponse;
import com.stayease.property_service.dto.response.PropertySearchResponse;
import com.stayease.property_service.entity.PropertyStatus;
import org.springframework.data.domain.Page;
import java.util.List;

public interface PropertyService {
    List<PropertyResponse> getPropertiesByOwner(Long OwnerId);
    Long countPropertiesByOwner(Long ownerId);
    PropertyResponse createProperty(PropertyRequest request);
    PropertyResponse getPropertyById(Long propertyId);
    PropertyResponse updateProperty(Long propertyId,UpdatePropertyRequest request);
    Page<PropertySearchResponse> searchProperties(PropertySearchRequest request);
    //Admin Operations Related to Property
    List<PendingPropertyResponse> getPendingProperties();
    PropertyResponse approveProperty(Long propertyId,ApprovePropertyRequest request);
    List<PendingPropertyResponse> getApprovedProperties();
    PropertyResponse rejectProperty(Long propertyId,RejectPropertyRequest request);
    List<PendingPropertyResponse> getRejectedProperties();
    PropertyResponse activateProperty(Long propertyId);
    PropertyResponse deactivateProperty(Long propertyId);
    PropertyResponse deleteProperty(Long propertyId);
}