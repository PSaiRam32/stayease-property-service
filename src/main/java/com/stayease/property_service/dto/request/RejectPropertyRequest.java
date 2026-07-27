package com.stayease.property_service.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RejectPropertyRequest{
    @NotNull(message="Admin Id is required.")
    private Long adminId;
    @NotBlank(message="Rejection Reason is Required" )
    private String rejectionReason;
}