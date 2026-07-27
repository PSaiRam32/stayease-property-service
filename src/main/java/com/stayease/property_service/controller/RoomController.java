package com.stayease.property_service.controller;

import com.stayease.property_service.dto.response.ApiResponse;
import com.stayease.property_service.dto.request.RoomRequest;
import com.stayease.property_service.dto.response.RoomDetailsResponse;
import com.stayease.property_service.dto.response.RoomSummaryResponse;
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
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/addroom/{propertyId}")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Add Room to Property")
    public ResponseEntity<ApiResponse<String>> addRoom(@PathVariable Long propertyId,@Valid @RequestBody RoomRequest request){
        roomService.addRoom(propertyId,request);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Room added successfully", "Room added successfully"));
    }

    @GetMapping("/getroom/{propertyId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('USER')")
    @Operation(summary = "Get All Rooms for Property")
    public ResponseEntity<ApiResponse<List<Room>>> getRoomsByProperty(@PathVariable Long propertyId){
        List<Room> rooms=roomService.getRooms(propertyId);
        if (rooms==null || rooms.isEmpty()){
            log.info("No rooms available for property ID: {}", propertyId);
            return ResponseEntity.ok(new ApiResponse<>("SUCCESS",
                    "No rooms available for property id: " + propertyId, null));
        }
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Rooms fetched successfully", rooms));
    }

    //For CreateBooking and Reschedule booking
    @GetMapping("/{roomId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('USER')")
    @Operation(summary = "Room Details for CreateBooking and Reschedule booking")
    public ResponseEntity<ApiResponse<RoomDetailsResponse>> getRoom(@PathVariable Long roomId){
        RoomDetailsResponse response = roomService.getRoomDetails(roomId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Room details", response));
    }

    @GetMapping("/internal/{roomId}")
    @Operation(summary="Owner Internal - Get Room Summary")
    public ResponseEntity<ApiResponse<RoomSummaryResponse>> getRoomSummary(@PathVariable Long roomId){
        RoomSummaryResponse response=roomService.getRoomSummary(roomId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Room summary fetched successfully", response));
    }
}