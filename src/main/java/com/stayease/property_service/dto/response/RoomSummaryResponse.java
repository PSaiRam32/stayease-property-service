package com.stayease.property_service.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomSummaryResponse{
    private Long roomId;
    private String roomNumber;
}