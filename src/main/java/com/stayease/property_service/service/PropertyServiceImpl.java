package com.stayease.property_service.service;

import com.stayease.property_service.dto.request.*;
import com.stayease.property_service.dto.response.*;
import com.stayease.property_service.entity.Amenity;
import com.stayease.property_service.entity.Property;
import com.stayease.property_service.entity.PropertyStatus;
import com.stayease.property_service.entity.Room;
import com.stayease.property_service.exception.BusinessException;
import com.stayease.property_service.exception.ExternalServiceException;
import com.stayease.property_service.exception.ResourceNotFoundException;
import com.stayease.property_service.integration.OwnerServiceGateway;
import com.stayease.property_service.repository.PropertyRepository;
import com.stayease.property_service.repository.RoomRepository;
import com.stayease.property_service.specification.PropertySpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropertyServiceImpl implements PropertyService{

    private final PropertyRepository propertyRepository;
    private final OwnerServiceGateway ownerServiceGateway;
    private final RoomRepository roomRepository;

    @Override
    public List<PropertyResponse> getPropertiesByOwner(Long ownerId){
        log.debug("Fetching all properties for owner ID: {}",ownerId);
        List<PropertyResponse> properties=propertyRepository.findByOwnerIdAndDeletedFalse(ownerId)
                .stream()
                .map(this::mapToPropertyResponse)
                .collect(Collectors.toList());
        log.info("Found {} properties for owner ID: {}", properties.size(),ownerId);
        return properties;
    }

    @Override
    public Long countPropertiesByOwner(Long ownerId){
        log.debug("Counting properties for owner ID: {}", ownerId);
        Long count=propertyRepository.countByOwnerIdAndDeletedFalse(ownerId);
        log.info("Property count for owner ID {}: {}", ownerId, count);
        return count;
    }

    @Override
    public PropertyResponse createProperty(PropertyRequest request){
        log.info("Creating property for owner ID: {}", request.getOwnerId());
        log.debug("Property details: title={}, description={}  location={},",request.getPropertyTitle(),request.getDescription(),request.getLocation());
        validateOwnerEligibility(request.getOwnerId());
        validateDuplicateProperty(request);
        Property property=buildProperty(request);
        Property savedProperty=propertyRepository.save(property);
        log.info("Property created successfully with ID: {}",property.getPropertyId());
        return mapToPropertyResponse(savedProperty);
    }

    @Override
    public PropertyResponse getPropertyById(Long propertyId){
        log.debug("Fetching property with ID: {}",propertyId);
        Property property=getPropertyByIdOrThrow(propertyId);
        log.info("Property fetched successfully with ID: {}",propertyId);
        return mapToPropertyResponse(property);
    }

    @Override
    public PropertyResponse updateProperty(Long propertyId,UpdatePropertyRequest request){
        log.info("Updating property with ID: {}",propertyId);
        log.debug("Update request: title={}, location={}, description={}",request.getPropertyTitle(),request.getLocation(), request.getDescription());
        Property property=getPropertyByIdOrThrow(propertyId);
        updatePropertyDetails(property, request);
        Property updated=propertyRepository.save(property);
        log.info("Property updated successfully with ID: {}", updated.getPropertyId());
        return mapToPropertyResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PropertySearchResponse> searchProperties(PropertySearchRequest request){
        log.info("Searching properties with filters.");
        Sort sort=Sort.by(Sort.Direction.fromString(request.getSortDirection()),request.getSortBy());
        Pageable pageable=PageRequest.of(request.getPage(),request.getSize(),sort);
        Page<Property> properties=propertyRepository.findAll(PropertySpecification.search(request),pageable);
        log.info("Property search completed successfully.");
        return properties.map(this::mapToSearchResponse);
    }
    @Override
    @Transactional(readOnly = true)
    public List<PendingPropertyResponse> getPendingProperties(){
        log.info("Fetching pending properties.");
        List<PendingPropertyResponse> response=getPropertiesByStatus(PropertyStatus.PENDING);
        log.info("Found {} pending properties.",response.size());
        return response;
    }

    @Override
    @Transactional
    public PropertyResponse approveProperty(Long propertyId, ApprovePropertyRequest request){
        log.info("Admin {} approving property {}",request.getAdminId(),propertyId);
        Property property=getPropertyByIdOrThrow(propertyId);
        if (property.getStatus()==PropertyStatus.ACTIVE){
            log.info("Property {} already approved.", propertyId);
            return mapToPropertyResponse(property);
        }
        validatePendingProperty(property);
        property.setStatus(PropertyStatus.ACTIVE);
        property.setIsActive(true);
        property.setReviewedBy(request.getAdminId());
        property.setReviewedAt(LocalDateTime.now());
        property.setRejectionReason(null);
        property.setUpdatedAt(LocalDateTime.now());
        Property savedProperty=propertyRepository.save(property);
        log.info("Property {} approved successfully by admin {}.",savedProperty.getPropertyId(),request.getAdminId());
        return mapToPropertyResponse(savedProperty);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PendingPropertyResponse> getApprovedProperties(){
        log.info("Fetching approved properties.");
        List<PendingPropertyResponse> response=getPropertiesByStatus(PropertyStatus.ACTIVE);
        log.info("Found {} approved properties.",response.size());
        return response;
    }

    @Override
    @Transactional
    public PropertyResponse rejectProperty(Long propertyId, RejectPropertyRequest request){
        log.info("Admin {} reject property {}",request.getAdminId(),propertyId);
        Property property=getPropertyByIdOrThrow(propertyId);
        if (property.getStatus()==PropertyStatus.REJECTED){
            log.info("Property {} already rejected.", propertyId);
            return mapToPropertyResponse(property);
        }
        validatePendingProperty(property);
        property.setStatus(PropertyStatus.REJECTED);
        property.setIsActive(false);
        property.setReviewedBy(request.getAdminId());
        property.setReviewedAt(LocalDateTime.now());
        property.setRejectionReason(request.getRejectionReason());
        property.setUpdatedAt(LocalDateTime.now());
        Property savedProperty=propertyRepository.save(property);
        log.info("Property {} rejected successfully by admin {}.",savedProperty.getPropertyId(),request.getAdminId());
        return mapToPropertyResponse(savedProperty);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PendingPropertyResponse> getRejectedProperties(){
        log.info("Fetching rejected properties.");
        List<PendingPropertyResponse> response=getPropertiesByStatus(PropertyStatus.REJECTED);
        log.info("Found {} rejected properties.",response.size());
        return response;
    }

    @Override
    @Transactional
    public PropertyResponse activateProperty(Long propertyId){
        Property property=getPropertyByIdOrThrow(propertyId);
        validateOwnerEligibility(property.getOwnerId());
        validatePropertyActivation(property);
        if (property.getStatus()==PropertyStatus.ACTIVE){
            log.info("Property {} already active.",propertyId);
            return mapToPropertyResponse(property);
        }
        property.setStatus(PropertyStatus.ACTIVE);
        property.setIsActive(true);
        property.setUpdatedAt(LocalDateTime.now());
        Property savedProperty=propertyRepository.save(property);
        log.info("Property {} activated successfully",propertyId);
        return mapToPropertyResponse(savedProperty);
    }

    @Override
    @Transactional
    public PropertyResponse deactivateProperty(Long propertyId){
        Property property=getPropertyByIdOrThrow(propertyId);
        validateOwnerEligibility(property.getOwnerId());
        validatePropertyActivation(property);
        if(property.getStatus()==PropertyStatus.INACTIVE){
            log.info("Property {} already inactive.",propertyId);
            return mapToPropertyResponse(property);
        }
        property.setStatus(PropertyStatus.INACTIVE);
        property.setIsActive(false);
        property.setUpdatedAt(LocalDateTime.now());
        Property savedProperty=propertyRepository.save(property);
        log.info("Property {} deactivated successfully", propertyId);
        return mapToPropertyResponse(savedProperty);
    }

    @Override
    @Transactional
    public PropertyResponse deleteProperty(Long propertyId){
        log.info("Deleting property {}", propertyId);
        Property property=getPropertyByIdOrThrow(propertyId);
        if(property.getDeleted()){
            log.info("Property {} already deleted.",propertyId);
            return mapToPropertyResponse(property);
        }
        property.setDeleted(true);
        property.setStatus(PropertyStatus.INACTIVE);
        property.setIsActive(false);
        property.setUpdatedAt(LocalDateTime.now());
        Property saved=propertyRepository.save(property);
        log.info("Property {} deleted successfully.", saved.getPropertyId());
        return mapToPropertyResponse(saved);
    }

    @Override
    public PropertySummaryResponse getPropertySummary(Long propertyId){
        Property property=propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        return PropertySummaryResponse.builder()
                .propertyId(property.getPropertyId())
                .propertyName(property.getPropertyTitle())
                .build();
    }

    @Override
    public OwnerRoomStatisticsResponse getTotalRoomsByOwner(Long ownerId){
        List<Property> properties=propertyRepository.findByOwnerIdAndDeletedFalse(ownerId);
        long totalRooms=properties.stream()
                .map(Property::getRooms)
                .filter(Objects::nonNull)
                .mapToLong(List::size)
                .sum();
        return OwnerRoomStatisticsResponse.builder()
                .totalRooms(totalRooms)
                .build();
    }

    //Validation Helper Methods

    private void validateOwnerEligibility(Long ownerId){
        OwnerResponse owner=ownerServiceGateway.getOwnerById(ownerId);
        if (owner==null){
            log.error("Owner not found with ID: {}", ownerId);
            throw new ResourceNotFoundException("Owner not found");
        }
        log.debug("Owner found: {}", owner);
        String kycStatus=ownerServiceGateway.getKycStatus(ownerId);
        log.debug("KYC status for owner ID {}: {}", ownerId, kycStatus);
        if (!"VERIFIED".equals(kycStatus)){
            log.error("Owner KYC not verified for owner ID: {}", ownerId);
            throw new BusinessException("Owner KYC not verified");
        }
    }

    private void validatePendingProperty(Property property){
        if(property.getDeleted()){
            log.error("Deleted property {} cannot be reviewed.",property.getPropertyId());
            throw new BusinessException("Deleted property cannot be reviewed.");
        }
        if(property.getStatus()!=PropertyStatus.PENDING){
            log.error("Only pending properties can be reviewed. Current status: {}",property.getStatus());
            throw new BusinessException("Only pending properties can be reviewed.");
        }
    }

    private void validatePropertyActivation(Property property){
        if(property.getDeleted()){
            throw new BusinessException("Deleted property cannot be activated.");
        }
        if(property.getStatus()!=PropertyStatus.ACTIVE && property.getStatus()!=PropertyStatus.INACTIVE){
            throw new BusinessException("Property must be approved before activation.");
        }
    }

   //Repository Helpers

    private void validateDuplicateProperty(PropertyRequest request){
        if (propertyRepository.existsByOwnerIdAndPropertyTitleIgnoreCaseAndLocationIgnoreCaseAndDeletedFalse(
                request.getOwnerId(),request.getPropertyTitle(),request.getLocation())) {
            throw new BusinessException("Property already exists for this owner at the same location.");
        }
    }

    private  Property getPropertyByIdOrThrow(Long propertyId){
        return propertyRepository.findByPropertyIdAndDeletedFalse(propertyId).orElseThrow(() -> {
            log.error("Property not found with ID to update the details: {}", propertyId);
            return new ResourceNotFoundException("Property not found");
        });
    }

    private List<PendingPropertyResponse> getPropertiesByStatus(PropertyStatus status){
        log.debug("Fetching properties with status {}",status);
        List<Property> properties=propertyRepository.findByStatusAndDeletedFalse(status);
        if(properties.isEmpty()){
            log.debug("No properties found with status {}",status);
            return List.of();
        }
        List<Long> ownerIds = properties.stream()
                .map(Property::getOwnerId)
                .distinct()
                .toList();
        List<OwnerResponse> owners=ownerServiceGateway.getOwnersByIds(ownerIds);
        if(owners.isEmpty()){
            throw new ExternalServiceException("Unable to fetch owner details.");
        }
        log.debug("Fetched {} owners from Owner Service.",owners.size());
        Map<Long, OwnerResponse> ownerMap=owners.stream()
                        .collect(Collectors.toMap(OwnerResponse::getOwnerId,Function.identity()));
        return properties.stream()
                .map(property -> mapToPendingPropertyResponse(
                                property,ownerMap.get(property.getOwnerId())))
                .toList();
    }

    //Entity Builder Helpers

    private Property buildProperty(PropertyRequest request){
        return Property.builder()
                .ownerId(request.getOwnerId())
                .propertyTitle(request.getPropertyTitle())
                .location(request.getLocation())
                .description(request.getDescription())
                .averageRating(0.0)
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .status(PropertyStatus.PENDING)
                .deleted(false)
                .isActive(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private void updatePropertyDetails(Property property,UpdatePropertyRequest request){
        property.setPropertyTitle(request.getPropertyTitle());
        property.setDescription(request.getDescription());
        property.setLocation(request.getLocation());
        property.setCity(request.getCity());
        property.setState(request.getState());
        property.setCountry(request.getCountry());
        property.setUpdatedAt(LocalDateTime.now());
    }

    //Mapper Helpers

    private List<RoomResponse> mapToRoomResponses(List<Room> rooms,Property property,OwnerResponse owner){
        return rooms.stream().map(room -> RoomResponse.builder()
                        .roomId(room.getRoomId())
                        .roomNumber(room.getRoomNumber())
                        .propertyId(property.getPropertyId())
                        .propertyName(property.getPropertyTitle())
                        .ownerName(owner.getName())
                        .phoneNumber(owner.getPhone())
                        .sharingCapacity(room.getSharingCapacity())
                        .price(room.getPrice())
//                        .availableCount(room.getAvailableCount())
                        .build())
                .toList();
    }
    private List<AmenityResponse> mapToAmenityResponses(Set<Amenity> amenities){
        if (amenities == null || amenities.isEmpty()){
            return List.of();
        }
        return amenities.stream().map(amenity -> AmenityResponse.builder()
                        .amenityId(amenity.getAmenityId())
                        .name(amenity.getName())
                        .build())
                .toList();
    }

    private PropertyResponse mapToPropertyResponse(Property property){
        log.debug("Mapping property entity to DTO for property ID: {}", property.getPropertyId());
        OwnerResponse owner=ownerServiceGateway.getOwnerById(property.getOwnerId());
        List<Room> propertyRooms=roomRepository.findByProperty_PropertyId(property.getPropertyId());
        List<RoomResponse> rooms=mapToRoomResponses(propertyRooms,property,owner);
        List<AmenityResponse> amenities=mapToAmenityResponses(property.getAmenities());
        return PropertyResponse.builder()
                .propertyId(property.getPropertyId())
                .ownerId(property.getOwnerId())
                .ownerName(owner.getName())
                .phoneNumber(owner.getPhone())
                .propertyTitle(property.getPropertyTitle())
                .description(property.getDescription())
                .location(property.getLocation())
                .city(property.getCity())
                .state(property.getState())
                .country(property.getCountry())
                .averageRating(property.getAverageRating())
                .rooms(rooms)
                .amenities(amenities)
                .status(property.getStatus())
                .build();
    }

    private PropertySearchResponse mapToSearchResponse(Property property){
        if (property.getRooms()==null || property.getRooms().isEmpty()) {
            throw new BusinessException("Property has no rooms.");
        }
        Double startingPrice=property.getRooms()
                .stream()
                .filter(room ->room.getAvailableCount()>0)
                .map(Room::getPrice)
                .min(Double::compareTo)
                .orElseThrow(() -> new BusinessException("Property has no available rooms."));
        List<String> amenities=property.getAmenities()
                .stream()
                .map(Amenity::getName)
                .sorted()
                .toList();
        return PropertySearchResponse.builder()
                .propertyId(property.getPropertyId())
                .propertyTitle(property.getPropertyTitle())
                .description(property.getDescription())
                .city(property.getCity())
                .state(property.getState())
                .country(property.getCountry())
                .startingPrice(startingPrice)
                .averageRating(property.getAverageRating())
                .status(property.getStatus())
                .amenities(amenities)
                .build();
    }

    private PendingPropertyResponse mapToPendingPropertyResponse(Property property,OwnerResponse owner){
        return PendingPropertyResponse.builder()
                .propertyId(property.getPropertyId())
                .propertyTitle(property.getPropertyTitle())
                .description(property.getDescription())
                .city(property.getCity())
                .state(property.getState())
                .country(property.getCountry())
                .status(property.getStatus())
                .createdAt(property.getCreatedAt())
                .ownerId(owner.getOwnerId())
                .ownerName(owner.getName())
                .ownerEmail(owner.getEmail())
                .ownerPhone(owner.getPhone())
                .build();
    }
}
