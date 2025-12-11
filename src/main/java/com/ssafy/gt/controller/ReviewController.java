package com.ssafy.gt.controller;

import com.ssafy.gt.dto.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ssafy.gt.service.ReviewService;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestPart(value = "images", required = false)List <MultipartFile> images){
            System.out.println("리뷰 내용: " + review);
            if(images!=null){
                System.out.println(images.size() + "개의 이미지");
                for(MultipartFile file : images){
                    System.out.println(file.getOriginalFilename());
                }
            }
            reviewService.createReview(review,images);
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
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateReview(
            @PathVariable int id,
            @ModelAttribute Review review,
            @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages,
            @RequestPart(value = "deleteImageNames", required = false) List<String> deleteImageNames
    ) {
        review.setId(id);

        reviewService.updateReview(review, newImages, deleteImageNames);

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