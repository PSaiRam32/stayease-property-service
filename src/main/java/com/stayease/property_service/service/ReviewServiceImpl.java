package com.stayease.property_service.service;

import com.stayease.property_service.config.UserClient;
import com.stayease.property_service.dto.request.ReviewRequest;
import com.stayease.property_service.dto.request.UpdateReviewRequest;
import com.stayease.property_service.dto.response.ReviewResponse;
import com.stayease.property_service.dto.response.UserResponse;
import com.stayease.property_service.entity.Property;
import com.stayease.property_service.entity.PropertyStatus;
import com.stayease.property_service.entity.Review;
import com.stayease.property_service.exception.BusinessException;
import com.stayease.property_service.exception.DuplicateResourceException;
import com.stayease.property_service.exception.ResourceNotFoundException;
import com.stayease.property_service.exception.UnauthorizedOperationException;
import com.stayease.property_service.repository.PropertyRepository;
import com.stayease.property_service.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final PropertyRepository propertyRepository;
    private final UserClient userClient;


    @Override
    @Transactional
    public ReviewResponse addReview(ReviewRequest request){
        log.info("Adding review for property {}", request.getPropertyId());
        Property property=getProperty(request.getPropertyId());
        validateProperty(property);
        validateDuplicateReview(request.getPropertyId(),request.getUserId());
        validateOwnerReview(property, request.getUserId());
        userClient.getUser(request.getUserId());
        Review review = Review.builder()
                .propertyId(request.getPropertyId())
                .userId(request.getUserId())
                .rating(request.getRating())
                .review(request.getReview())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Review saved = reviewRepository.save(review);
        updateAverageRating(property.getPropertyId());
        log.info("Review {} created successfully", saved.getReviewId());
        return mapToReviewResponse(saved);
    }


    @Override
    @Transactional
    public ReviewResponse updateReview(UpdateReviewRequest request){
        log.info("Updating review {}", request.getReviewId());
        Review review=getReview(request.getReviewId());
        validateReviewOwner(review,request.getUserId());
        review.setRating(request.getRating());
        review.setReview(request.getReview());
        review.setUpdatedAt(LocalDateTime.now());
        Review updatedReview = reviewRepository.save(review);
        updateAverageRating(review.getPropertyId());
        log.info("Review {} updated successfully", review.getReviewId());
        return mapToReviewResponse(updatedReview);
    }


    @Override
    @Transactional
    public void deleteReview(Long reviewId,Long userId){
        log.info("Deleting review {}", reviewId);
        Review review=getReview(reviewId);
        validateReviewOwner(review,userId);
        Long propertyId=review.getPropertyId();
        reviewRepository.delete(review);
        updateAverageRating(propertyId);
        log.info("Review {} deleted successfully", reviewId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByProperty(Long propertyId){
        log.info("Fetching reviews for property {}", propertyId);
        Property property=getProperty(propertyId);
        validateProperty(property);
        List<Review> reviews = reviewRepository.findByPropertyId(propertyId);
        if (reviews.isEmpty()) {
            log.info("No reviews found for property {}", propertyId);
            return List.of();
        }
        return reviews.stream()
                .map(this::mapToReviewResponse)
                .toList();
    }

    //Helper Methods

    private void validateReviewOwner(Review review, Long userId) {
        if (!review.getUserId().equals(userId)) {
            throw new UnauthorizedOperationException("You are not authorized to perform an action on this review.");
        }
    }

    private Property getProperty(Long propertyId){
        return propertyRepository.findByPropertyIdAndDeletedFalse(propertyId)
                .orElseThrow(() -> {log.error("Property {} not found", propertyId);
                    return new ResourceNotFoundException("Property not found.");
                });
    }

    private void validateProperty(Property property){
        if(property.getDeleted()){
            throw new BusinessException("Deleted property cannot be reviewed.");
        }
        if(property.getStatus()!=PropertyStatus.ACTIVE){
            throw new BusinessException("Only active properties can be reviewed.");
        }
    }

    private void validateDuplicateReview(Long propertyId,Long userId){
        if(reviewRepository.existsByPropertyIdAndUserId(propertyId,userId)){
            throw new DuplicateResourceException("You have already reviewed this property.");
        }
    }
    private void validateOwnerReview(Property property,Long userId){
        if(property.getOwnerId().equals(userId)){
            throw new UnauthorizedOperationException("Owner cannot review own property.");
        }
    }

    private void updateAverageRating(Long propertyId){
        Double average=reviewRepository.calculateAverageRating(propertyId);
        Property property=getProperty(propertyId);
        property.setAverageRating(average==null?0.0:average);
        propertyRepository.save(property);
    }

    private Review getReview(Long reviewId){
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> {log.error("Review {} not found", reviewId);
                    return new ResourceNotFoundException("Review not found.");
                });
    }

    private ReviewResponse mapToReviewResponse(Review review){
        UserResponse user=userClient.getUser(review.getUserId());
        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .propertyId(review.getPropertyId())
                .userId(review.getUserId())
                .userName(user.getName())
                .rating(review.getRating())
                .review(review.getReview())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }


}
