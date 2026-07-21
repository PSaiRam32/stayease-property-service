package com.stayease.property_service.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews",uniqueConstraints = {@UniqueConstraint(columnNames = {"property_id", "user_id"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;
    @Column(name = "property_id", nullable = false)
    private Long propertyId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer rating;
    @Column(length = 1000)
    private String review;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
