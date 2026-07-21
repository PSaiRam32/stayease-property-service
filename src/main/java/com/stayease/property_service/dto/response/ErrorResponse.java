package com.stayease.property_service.dto.response;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse{
    private int status;
    private String message;
    private LocalDateTime timestamp;
}