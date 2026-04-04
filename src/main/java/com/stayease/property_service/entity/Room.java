package com.stayease.property_service.entity;

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
    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
}