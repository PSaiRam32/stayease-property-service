package com.stayease.property_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer capacity;
    private Double price;
    private Integer availableCount;
    @ManyToOne
    @JoinColumn(name = "properties_property_id",nullable = false)
    @JsonBackReference
    private Property property;
    @Version
    private long version;
}