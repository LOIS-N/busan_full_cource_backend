package com.ssafy.gt.controller;

import com.ssafy.gt.dto.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ssafy.gt.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    /**
     * 리뷰 등록
     * POST /api/v1/review/postReview
     */
    @PostMapping
    public ResponseEntity<Review> addReview(@RequestBody Review review){
        reviewService.createReview(review);
        return ResponseEntity.ok(review);
    }
    /**
     * 리뷰 불러오기
     * GET /api/v1/review/getReviewByTarget?targetId={targetId}&targetType={targetType}
     */
    @GetMapping("/getReviewByTarget")
    public ResponseEntity<List<Review>> getReviewsByTarget(
            @RequestParam int targetId,
            @RequestParam int targetType
    ) {
        List<Review> reviews = reviewService.getReviewsByTarget(targetId,targetType);
        return ResponseEntity.ok(reviews);
    }

    /**
     * 본인 작성 리뷰 불러오기
     * GET /api/v1/review/getReviewByUserId?userId={userId}
     */
    @GetMapping("/getReviewByUserId")
    public ResponseEntity<List<Review>> getReviewsByUserId(@RequestParam int userId){
        List<Review> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * 리뷰 수정하기
     * PUT /api/v1/review/{id}
     */
    @PutMapping("/{id}")
    public  ResponseEntity<Void> updateReview(
            @PathVariable int id,
            @RequestBody Review review
    ){
        review.setId(id);
        reviewService.update(review);
        return ResponseEntity.ok().build();
    }
    /**
     * 리뷰 삭제
     * DELETE /api/v1/review/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable int id){
        reviewService.delete(id);
        return ResponseEntity.ok().build();
    }


}