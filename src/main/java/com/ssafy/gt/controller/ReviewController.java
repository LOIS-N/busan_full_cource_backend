package com.ssafy.gt.controller;

import com.ssafy.gt.dto.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.ssafy.gt.service.ReviewService;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    /**
     * 리뷰 등록
     * POST /api/v1/review/
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Review> addReview(
            @ModelAttribute Review review,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer userId = Integer.valueOf(authentication.getName());

        review.setUserId(userId);
        review.setCreatedAt(LocalDateTime.now());

        reviewService.createReview(review, images);

        return ResponseEntity.ok(review);
    }
    /**
     * 리뷰 불러오기
     * GET /api/v1/review/getReviewByTarget?targetId={targetId}&targetType={targetType}
     */
    @GetMapping("/getReviewsByTarget")
    public ResponseEntity<List<Review>> getReviewsByTarget(
            @RequestParam int targetId,
            @RequestParam String targetType
    ) {
        List<Review> reviews = reviewService.getReviewsByTarget(targetId,targetType);
        return ResponseEntity.ok(reviews);
    }

    /**
     * 본인 작성 리뷰 불러오기
     * GET /api/v1/review/getReviewByUserId
     */
    @GetMapping("/getReviewByUserId")
    public ResponseEntity<List<Review>> getMyReviews(
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer userId = Integer.valueOf(authentication.getName());
        return ResponseEntity.ok(reviewService.getReviewsByUserId(userId));
    }

    /**
     * 리뷰 수정하기
     * PUT /api/v1/review/{id}
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateReview(
            @PathVariable int id,
            @ModelAttribute Review review,
            @RequestPart(value = "images", required = false) List<MultipartFile> newImages,
            @RequestPart(value = "keepImageIds", required = false) List<Long> keepImageIds
    ) {
        review.setId(id);
        reviewService.updateReview(review, newImages, keepImageIds);
        return ResponseEntity.ok().build();
    }

    /**
     * 리뷰 삭제
     * DELETE /api/v1/review/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable int id,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer userId = Integer.valueOf(authentication.getName());

        reviewService.deleteReview(id, userId);

        return ResponseEntity.ok().build();
    }



}