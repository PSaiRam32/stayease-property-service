package com.stayease.property_service.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "property_amenity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyAmenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long propertyId;
    private Long amenityId;
}