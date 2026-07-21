package com.stayease.property_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovePropertyRequest{
    @NotNull(message = "Admin Id is required.")
    private Long adminId;
}