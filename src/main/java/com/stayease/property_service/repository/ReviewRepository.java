package com.stayease.property_service.repository;

import com.stayease.property_service.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>{
//    Optional<Review> findByPropertyIdAndUserId(Long propertyId, Long userId);
    boolean existsByPropertyIdAndUserId(Long propertyId, Long userId);
    List<Review> findByPropertyId(Long propertyId);
//    Long countByPropertyId(Long propertyId);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.propertyId = :propertyId")
    Double calculateAverageRating(@Param("propertyId") Long propertyId);
}
