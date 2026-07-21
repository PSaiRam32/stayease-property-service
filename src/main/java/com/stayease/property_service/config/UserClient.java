package com.stayease.property_service.config;


import com.stayease.property_service.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        url = "${services.user.url}",
        configuration = FeignConfig.class
)
public interface UserClient {
    @GetMapping("/users/{userId}")
    UserResponse getUser(@PathVariable Long userId);
}