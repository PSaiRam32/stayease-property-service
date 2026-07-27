package com.stayease.property_service.service;

import com.stayease.property_service.dto.request.ReviewRequest;
import com.stayease.property_service.dto.request.UpdateReviewRequest;
import com.stayease.property_service.dto.response.ReviewResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface ReviewService{
    ReviewResponse addReview(ReviewRequest request);
    ReviewResponse updateReview(UpdateReviewRequest request);
    void deleteReview(Long reviewId,Long userId);
    List<ReviewResponse> getReviewsByProperty(Long propertyId);
}