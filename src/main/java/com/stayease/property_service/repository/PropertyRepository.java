package com.stayease.property_service.repository;

import com.stayease.property_service.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {
    //JpaSpecificationExecutor -Dynamic Sql - Only adds filters provided by user
    Optional<Property> findByPropertyIdAndDeletedFalse(Long propertyId);
    List<Property> findByOwnerIdAndDeletedFalse(Long ownerId);
    Long countByOwnerIdAndDeletedFalse(Long ownerId);
    List<Property> findByStatusAndDeletedFalse(PropertyStatus status);
}