package com.ssafy.gt.mapper;

import com.ssafy.gt.dto.Review;

import java.math.BigDecimal;
import java.util.List;

public interface ReviewMapper {
    int insert(Review review);
    List<Review> getReviewsByTarget(int targetId, String targetType);
    List<Review> getReviewsByUserId(int UserId);
    Review getReviewById(int reviewId);
    int deleteById(int reviewId);
    int update(Review review);
    int delete(int id);
    BigDecimal getAverageRatingByTarget(int TargetId);

}
