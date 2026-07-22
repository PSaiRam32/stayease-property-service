package com.stayease.property_service.service;

import com.stayease.property_service.dto.request.RoomRequest;
import com.stayease.property_service.dto.response.RoomDetailsResponse;
import com.stayease.property_service.entity.Room;

import java.util.List;

public interface RoomService {

    void addRoom(Long propertyId, RoomRequest request);
    List<Room> getRooms(Long propertyId);
//    Boolean reserveRoom(Long roomId);
//    void releaseRoom(Long roomId);
//    Boolean checkAvailability(Long roomId);
    RoomDetailsResponse getRoomDetails(Long roomId);
}
