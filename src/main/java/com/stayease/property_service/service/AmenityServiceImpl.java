package com.stayease.property_service.service;

import com.stayease.property_service.dto.request.AmenityRequest;
import com.stayease.property_service.dto.response.AmenityResponse;
import com.stayease.property_service.entity.Amenity;
import com.stayease.property_service.entity.Property;
import com.stayease.property_service.exception.BusinessException;
import com.stayease.property_service.exception.ResourceNotFoundException;
import com.stayease.property_service.repository.AmenityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.stayease.property_service.repository.PropertyRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AmenityServiceImpl implements AmenityService{

	private final AmenityRepository amenityRepository;
	private final PropertyRepository propertyRepository;

	@Override
	public AmenityResponse createAmenity(AmenityRequest request){
		log.info("Creating amenity: {}", request.getName());
		validateDuplicateAmenity(request.getName());
		Amenity amenity=Amenity.builder()
				.name(request.getName())
				.build();
		Amenity savedAmenity=amenityRepository.save(amenity);
		return AmenityResponse.builder()
				.amenityId(savedAmenity.getAmenityId())
				.name(savedAmenity.getName())
				.build();
	}

	@Override
	public List<AmenityResponse> getAllAmenities(){
		return amenityRepository.findAll()
				.stream()
				.map(a -> AmenityResponse.builder()
						.amenityId(a.getAmenityId())
						.name(a.getName())
						.build()
				)
				.collect(Collectors.toList());
	}

	@Override
	public AmenityResponse getAmenityById(Long amenityId){
		Amenity a=amenityRepository.findById(amenityId)
				.orElseThrow(() -> {
					log.error("Amenity not found with ID: {}", amenityId);
					return new ResourceNotFoundException("Amenity not found.");
				});
		return AmenityResponse.builder()
				.amenityId(a.getAmenityId())
				.name(a.getName())
				.build();
	}

	@Override
	public void linkAmenitiesToProperty(Long propertyId,List<Long> amenityIds){
		log.info("Linking {} amenities to property ID: {}", amenityIds.size(), propertyId);
		Property property=propertyRepository.findByPropertyIdAndDeletedFalse(propertyId)
				.orElseThrow(()->{
					log.error("Property not found with ID: {}", propertyId);
					return new ResourceNotFoundException("Property not found.");
				});
		Set<Amenity> currentAmenities=property.getAmenities();
		if (currentAmenities==null){
			currentAmenities=new HashSet<>();
		}
		List<Amenity> amenities=amenityRepository.findAllById(amenityIds);
		if (amenities.size()!=amenityIds.size()){
			throw new ResourceNotFoundException("One or more amenities were not found.");
		}
		currentAmenities.addAll(amenities);
		property.setAmenities(currentAmenities);
		propertyRepository.save(property);
	}

	private void validateDuplicateAmenity(String name){
		if(amenityRepository.existsByNameIgnoreCase(name)){
			throw new BusinessException("Amenity already exists.");
		}
	}
}



