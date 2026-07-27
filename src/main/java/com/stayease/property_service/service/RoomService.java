package com.stayease.property_service.service;

import com.stayease.property_service.dto.request.RoomRequest;
import com.stayease.property_service.dto.response.RoomDetailsResponse;
import com.stayease.property_service.dto.response.RoomSummaryResponse;
import com.stayease.property_service.entity.Room;

import java.util.List;

public interface RoomService {

    void addRoom(Long propertyId, RoomRequest request);
    List<Room> getRooms(Long propertyId);
    RoomDetailsResponse getRoomDetails(Long roomId);
    RoomSummaryResponse getRoomSummary(Long roomId);
}
