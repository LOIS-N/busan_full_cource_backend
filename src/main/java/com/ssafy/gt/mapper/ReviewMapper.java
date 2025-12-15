package com.ssafy.gt.mapper;

import com.ssafy.gt.dto.Review;

import java.util.List;

public interface ReviewMapper {
    int insert(Review review);
    List<Review> getReviewsByTarget(int targetId, String targetType);
    List<Review> getReviewsByUserId(int UserId);
    int update(Review review);
    int delete(int id);
    double getAverageRatingByTarget(int TargetId, int TargetType);

}
