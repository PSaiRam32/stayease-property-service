package com.stayease.property_service.service;

import com.stayease.property_service.config.OwnerClient;
import com.stayease.property_service.dto.OwnerResponseDTO;
import com.stayease.property_service.dto.PropertyRequestDTO;
import com.stayease.property_service.dto.RoomRequestDTO;
import com.stayease.property_service.entity.Property;
import com.stayease.property_service.entity.Room;
import com.stayease.property_service.exception.BusinessException;
import com.stayease.property_service.repository.PropertyRepository;
import com.stayease.property_service.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final OwnerClient ownerClient;
    private final PropertyRepository propertyRepository;

    @Override
    public void addRoom(Long propertyId, RoomRequestDTO request) {
        log.info("Adding room to property ID: {}", propertyId);
        log.debug("Room details: capacity={}, price={}", request.getCapacity(), request.getPrice());
        Property property = propertyRepository.findByPropertyId(propertyId)
                .orElseThrow(() -> {
                    log.error("Property not found with propertyID: {}", propertyId);
                    return new BusinessException("Property not found");
                });
        PropertyRequestDTO prd= new PropertyRequestDTO();
        OwnerResponseDTO owner = ownerClient.getOwnerById(prd.getOwnerId());
        if (owner == null) {
            log.error("Owner not found with ID: {}",prd.getOwnerId());
            throw new BusinessException("Owner not found");
        }
        log.debug("Owner found: {}", owner);
        Room room = Room.builder()
                .capacity(request.getCapacity())
                .price(request.getPrice())
                .availableCount(request.getCapacity())
                .property(property)
                .build();
        roomRepository.save(room);
        log.info("Room added successfully to property ID: {}", propertyId);
    }

    @Override
    public List<Room> getRooms(Long propertyId) {
        log.debug("Fetching rooms for property ID: {}", propertyId);
        List<Room> rooms = roomRepository.findByProperty_PropertyId(propertyId);
        log.info("Found {} rooms for property ID: {}", rooms.size(), propertyId);
        return rooms;
    }

    @Override
    @Transactional
    public Boolean reserveRoom(Long roomId) {
        log.info("Reserving room with ID: {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> {
                    log.error("Room not found with ID: {}", roomId);
                    return new BusinessException("Room not found");
                });
        log.debug("Current room availability status: {}", room.getAvailableCount());
        if (room.getAvailableCount() <= 0) {
            log.warn("Room already booked - cannot reserve room ID: {}", roomId);
            throw new BusinessException("Room already booked - no availability");
        }
        room.setAvailableCount(room.getAvailableCount() - 1);
        roomRepository.save(room);
        log.info("Room reserved successfully with ID: {}", roomId);
        return true;
    }

    @Override
    public void releaseRoom(Long roomId) {
        log.info("Releasing room with ID: {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> {
                    log.error("Room not found with Room ID: {}", roomId);
                    return new BusinessException("Room not found");
                });
        room.setAvailableCount(room.getAvailableCount() + 1);
        roomRepository.save(room);
        log.info("Room released successfully with Room ID: {}", roomId);
    }

    @Override
    public Boolean checkAvailability(Long roomId) {
        log.debug("Checking availability for room ID: {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> {
                    log.error("Room not found with RoomID: {}", roomId);
                    return new BusinessException("Room not found");
                });
        log.debug("Room {} has {} available slots", roomId, room.getAvailableCount());
        return room.getAvailableCount() > 0;
    }


//    private void validateOwnership(Property property) {
//        String userIdHeader = SecurityContextHolder.getContext().getAuthentication().getName();
//        Long userId = Long.parseLong(userIdHeader);
//
//        if (!property.getOwnerId().equals(userId)) {
//            log.warn("Unauthorized access attempt: User ID {} tried to access property owned by {}", userId, property.getOwnerId());
//            throw new AccessDeniedException("You do not have permission to modify this property");
//        }
//        log.debug("Ownership validation passed for user ID {} on property ID {}", userId, property.getPropertyId());
//    }
}

