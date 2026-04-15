package com.stayease.property_service.service;

import com.stayease.property_service.dto.AmenityResponseDTO;
import com.stayease.property_service.entity.Amenity;
import com.stayease.property_service.repository.AmenityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.stayease.property_service.repository.PropertyRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AmenityServiceImpl implements AmenityService {

	private final AmenityRepository amenityRepository;
	private final PropertyRepository propertyRepository;

	@Override
	public AmenityResponseDTO createAmenity(Amenity amenity) {
		log.info("Creating amenity: {}", amenity.getName());
		Amenity saved = amenityRepository.save(amenity);
		return AmenityResponseDTO.builder()
				.id(saved.getId())
				.name(saved.getName())
				.build();
	}

	@Override
	public List<AmenityResponseDTO> getAllAmenities() {
		return amenityRepository.findAll()
				.stream()
				.map(a -> AmenityResponseDTO.builder().id(a.getId()).name(a.getName()).build())
				.collect(Collectors.toList());
	}

	@Override
	public AmenityResponseDTO getAmenityById(Long id) {
		Amenity a = amenityRepository.findById(id).orElseThrow(() -> new RuntimeException("Amenity not found"));
		return AmenityResponseDTO.builder().id(a.getId()).name(a.getName()).build();
	}

	@Override
	public void linkAmenitiesToProperty(Long propertyId, List<Long> amenityIds) {
		log.info("Linking {} amenities to property ID: {}", amenityIds.size(), propertyId);
		com.stayease.property_service.entity.Property property = propertyRepository.findByPropertyId(propertyId)
				.orElseThrow(() -> new RuntimeException("Property not found"));
		Set<Amenity> current = property.getAmenities();
		if (current == null) current = new java.util.HashSet<>();
		List<Amenity> amenities = amenityRepository.findAllById(amenityIds);
		current.addAll(amenities);
		property.setAmenities(current);
		propertyRepository.save(property);
	}
}



