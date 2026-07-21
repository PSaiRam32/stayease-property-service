package com.stayease.property_service.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "properties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private Long propertyId;
    private Long ownerId;
    @Column(nullable = false)
    private String propertyTitle;
    private String description;
    @Column(nullable = false)
    private String location;
    private String city;
    private String state;
    private String country;
    @Builder.Default
    private Double averageRating = 0.0;
    @Builder.Default
    private Boolean deleted = false;
    @Builder.Default
    private Boolean isActive = false;
//    private Double latitude;
//    private Double longitude;
//    private String thumbnailUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    private PropertyStatus status;
    private Long reviewedBy;
    private LocalDateTime reviewedAt;
    private String rejectionReason;
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Room> rooms;
    
    @ManyToMany
    @JoinTable(name = "property_amenity",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"property_id", "amenity_id"}))
    private Set<Amenity> amenities;
}

