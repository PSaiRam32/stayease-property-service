package com.stayease.property_service.controller;

import com.stayease.property_service.dto.request.ReviewRequest;
import com.stayease.property_service.dto.request.UpdateReviewRequest;
import com.stayease.property_service.dto.response.ApiResponse;
import com.stayease.property_service.dto.response.ReviewResponse;
import com.stayease.property_service.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller("/review")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Review Management", description = "APIs for managing reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/addreview")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary="Add Review")
    public ResponseEntity<ApiResponse<ReviewResponse>> addReview(@Valid @RequestBody ReviewRequest request){
        ReviewResponse response=reviewService.addReview(request);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Review added successfully",response));
    }

    @PutMapping("/updatereview")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary="Update Review")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(@Valid @RequestBody UpdateReviewRequest request){
        ReviewResponse response=reviewService.updateReview(request);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Review updated successfully",response));
    }

    @DeleteMapping("/deletereview/{reviewId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary="Delete Review")
    public ResponseEntity<ApiResponse<String>> deleteReview(@PathVariable Long reviewId,@RequestParam Long userId){
        reviewService.deleteReview(reviewId,userId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Review deleted successfully", "Review removed"));
    }

    @GetMapping("/getpropertyreviews/{propertyId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary="Get Property Reviews")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviews(@PathVariable Long propertyId){
        List<ReviewResponse> response=reviewService.getReviewsByProperty(propertyId);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", "Reviews fetched successfully",response));
    }
}
