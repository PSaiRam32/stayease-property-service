package com.stayease.property_service.controller;

import com.stayease.property_service.dto.ApiResponse;
import com.stayease.property_service.dto.RoomRequestDTO;
import com.stayease.property_service.entity.Room;
import com.stayease.property_service.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/properties/rooms")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Room Management", description = "APIs for managing Rooms")
public class InternalRoomController {

    private final RoomService roomService;

    @PostMapping("/addroom/{propertyId}")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Add Room to Property")
    public ResponseEntity<ApiResponse<String>> addRoom(
            @PathVariable Long propertyId,
            @Valid @RequestBody RoomRequestDTO request){
        log.info("Adding room to property ID: {}", propertyId);
        log.debug("Room request: capacity={}, price={}", request.getCapacity(), request.getPrice());
        roomService.addRoom(propertyId, request);
        log.info("Room added successfully to property ID: {}", propertyId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Room added successfully", null));
    }

    @GetMapping("/getroom/{propertyId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('USER')")
    @Operation(summary = "Get All Rooms for Property")
    public ResponseEntity<ApiResponse<List<Room>>> getRoomsByProperty(@PathVariable Long propertyId) {
        log.debug("Fetching rooms for property ID: {}", propertyId);
        List<Room> rooms = roomService.getRooms(propertyId);
        if (rooms == null || rooms.isEmpty()) {
            log.info("No rooms available for property ID: {}", propertyId);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS",
                    "No rooms available for property id: " + propertyId, null));
        }

        log.info("Fetched {} rooms for property ID: {}", rooms.size(), propertyId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Rooms fetched successfully", rooms));
    }

    @PutMapping("/reserverroom/{roomId}")
    @Operation(summary = "Reserve Room")
    public ResponseEntity<ApiResponse<Boolean>> reserveRoom(@PathVariable Long roomId) {
        log.info("Reserving room with ID: {}", roomId);
        Boolean result = roomService.reserveRoom(roomId);
        log.info("Room reserved successfully with ID: {}", roomId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Room reserved successfully", result));
    }

    @PutMapping("/releaseroom/{roomId}")
    @Operation(summary = "Release Room")
    public ResponseEntity<ApiResponse<String>> releaseRoom(@PathVariable Long roomId) {
        log.info("Releasing room with ID: {}", roomId);
        roomService.releaseRoom(roomId);
        log.info("Room released successfully with ID: {}", roomId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Room released successfully", roomId.toString()));
    }

    @GetMapping("/availability/{roomId}")
    @Operation(summary = "Check Room Availability")
    public ResponseEntity<ApiResponse<Boolean>> checkAvailability(@PathVariable Long roomId) {
        log.debug("Checking availability for room ID: {}", roomId);
        boolean available = roomService.checkAvailability(roomId);
        log.info("Room ID: {} availability status: {}", roomId, available);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Availability checked", available));
    }

}