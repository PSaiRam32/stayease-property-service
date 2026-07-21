package com.stayease.property_service.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequest{
    @NotNull(message="Property ID is required")
    private Long propertyId;
    @NotNull(message="User ID is required")
    private Long userId;
    @NotNull(message="Rating is required")
    @Min(value=1,message="Minimum rating is 1")
    @Max(value=5,message="Maximum rating is 5")
    private Integer rating;
    @NotBlank(message="Review cannot be empty")
    private String review;
}