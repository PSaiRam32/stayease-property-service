package com.stayease.property_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
@Entity
@Table(name = "amenities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Amenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long amenityId;
    private String name;
    @ManyToMany(mappedBy = "amenities")
    private Set<Property> properties;
}
