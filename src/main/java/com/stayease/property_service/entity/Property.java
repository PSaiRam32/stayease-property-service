package com.stayease.property_service.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "properties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private Long propertyId;
    private Long ownerId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String location;
    private String description;
    private Double rating;
    @Builder.Default
    private Boolean isActive = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Room> rooms;
    
    @ManyToMany
    @JoinTable(name = "property_amenity",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"property_id", "amenity_id"}))
    private java.util.Set<Amenity> amenities;
}

