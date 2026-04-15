package com.stayease.property_service.repository;

import com.stayease.property_service.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    Optional<Property> findByPropertyId(Long propertyId);
    List<Property> findByOwnerId(Long ownerId);
    Long countByOwnerId(Long ownerId);
}