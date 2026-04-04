package com.stayease.property_service.dto;

import lombok.*;


@Getter
@Setter
@Builder
public class PropertyResponseDTO {
    private Long id;
    private String ownerId;
    private String title;
    private String location;
    private String description;
}