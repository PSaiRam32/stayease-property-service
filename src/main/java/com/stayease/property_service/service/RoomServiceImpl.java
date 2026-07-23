package com.stayease.property_service.service;

import com.stayease.property_service.config.OwnerClient;
import com.stayease.property_service.dto.response.OwnerResponse;
import com.stayease.property_service.dto.request.RoomRequest;
import com.stayease.property_service.dto.response.RoomDetailsResponse;
import com.stayease.property_service.entity.Property;
import com.stayease.property_service.entity.Room;
import com.stayease.property_service.exception.BusinessException;
import com.stayease.property_service.exception.ExternalServiceException;
import com.stayease.property_service.exception.ResourceNotFoundException;
import com.stayease.property_service.repository.PropertyRepository;
import com.stayease.property_service.repository.RoomRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public void addRoom(Long propertyId, RoomRequest request){
        log.info("Adding room to property ID: {}", propertyId);
        log.debug("Room details: capacity={}, price={}", request.getSharingCapacity(), request.getPrice());
        Property property = propertyRepository.findByPropertyIdAndDeletedFalse(propertyId)
                .orElseThrow(() -> {
                    log.error("Property not found with propertyID: {}", propertyId);
                    return new ResourceNotFoundException("Property not found");
                });
        if (property.getOwnerId()==null) {
            throw new BusinessException("Property is not associated with any owner.");
        }
        try {
            OwnerResponse owner=ownerClient.getOwnerById(property.getOwnerId());
            if(owner==null){
                throw new ResourceNotFoundException("Owner not found");
            }
            log.debug("Owner found: {}", owner);
        }
        catch(FeignException ex){
            throw new ExternalServiceException("Owner Service is unavailable.");
        }
        Room room = Room.builder()
                .roomNumber(request.getRoomNumber())
                .sharingCapacity(request.getSharingCapacity())
                .price(request.getPrice())
                .availableCount(request.getAvailableCount())
                .washroomType(request.getWashroomType())
                .property(property)
                .build();
        roomRepository.save(room);
        log.info("Room added successfully to property ID: {}", propertyId);
    }

    @Override
    public List<Room> getRooms(Long propertyId){
        log.debug("Fetching rooms for property ID: {}", propertyId);
        List<Room> rooms=roomRepository.findByProperty_PropertyId(propertyId);
        log.info("Found {} rooms for property ID: {}", rooms.size(), propertyId);
        return rooms;
    }

//    @Override
//    @Transactional
//    public Boolean reserveRoom(Long roomId){
//        log.info("Reserving room with ID: {}", roomId);
//        Room room=roomRepository.findById(roomId)
//                .orElseThrow(() -> {
//                    log.error("Room not found with ID: {}", roomId);
//                    return new ResourceNotFoundException("Room not found");
//                });
//        log.debug("Current room availability status: {}", room.getAvailableCount());
//        if (room.getAvailableCount()<=0){
//            log.warn("Room already booked - cannot reserve room ID: {}", roomId);
//            throw new BusinessException("Room already booked - no availability");
//        }
//        room.setAvailableCount(room.getAvailableCount() - 1);
//        roomRepository.save(room);
//        log.info("Room reserved successfully with ID: {}", roomId);
//        return true;
//    }
//
//    @Override
//    public void releaseRoom(Long roomId){
//        log.info("Releasing room with ID: {}", roomId);
//        Room room = roomRepository.findById(roomId)
//                .orElseThrow(() -> {
//                    log.error("Room not found with Room ID: {}", roomId);
//                    return new BusinessException("Room not found");
//                });
//        room.setAvailableCount(room.getAvailableCount() + 1);
//        roomRepository.save(room);
//        log.info("Room released successfully with Room ID: {}", roomId);
//    }

//    @Override
//    public Boolean checkAvailability(Long roomId) {
//        log.debug("Checking availability for room ID: {}", roomId);
//        Room room = roomRepository.findById(roomId)
//                .orElseThrow(() -> {
//                    log.error("Room not found with RoomID: {}",roomId);
//                    return new BusinessException("Room not found");
//                });
//        log.debug("Room {} has {} available slots", roomId, room.getAvailableCount());
//        return room.getAvailableCount() > 0;
//    }

    @Override
    public RoomDetailsResponse getRoomDetails(Long roomId){
        Room room = roomRepository.findById(roomId).orElseThrow(() ->
                        new ResourceNotFoundException("Room not found"));
        Property property = room.getProperty();
        return RoomDetailsResponse.builder()
                .roomId(room.getRoomId())
                .propertyId(property.getPropertyId())
                .ownerId(property.getOwnerId())
                .sharingCapacity(room.getSharingCapacity())
                .price(room.getPrice())
//                .availableCount(room.getAvailableCount())
                .washroomType(room.getWashroomType())
                .propertyStatus(property.getStatus())
                .build();
    }
}

