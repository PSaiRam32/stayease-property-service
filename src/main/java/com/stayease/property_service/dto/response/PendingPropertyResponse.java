package com.stayease.property_service.dto.response;

import com.stayease.property_service.entity.PropertyStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PendingPropertyResponse{
    private Long propertyId;
    private String propertyTitle;
    private String description;
    private String city;
    private String state;
    private String country;
    private PropertyStatus status;
    private LocalDateTime createdAt;
    // Owner Details
    private Long ownerId;
    private String ownerName;
    private String ownerEmail;
    private String ownerPhone;
}