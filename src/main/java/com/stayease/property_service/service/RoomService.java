package com.stayease.property_service.service;

import com.stayease.property_service.dto.RoomRequestDTO;
import com.stayease.property_service.entity.Room;

import java.util.List;

public interface RoomService {

    void addRoom(Long propertyId, RoomRequestDTO request);
    List<Room> getRooms(Long propertyId);
    Boolean checkAvailability(Long roomId);
    Boolean reserveRoom(Long roomId);
    void releaseRoom(Long roomId);
}
