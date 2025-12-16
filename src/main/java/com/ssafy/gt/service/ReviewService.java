package com.ssafy.gt.service;

import com.ssafy.gt.dto.Review;
import com.ssafy.gt.dto.ReviewPicture;
import com.ssafy.gt.mapper.ReviewPictureMapper;
import com.ssafy.gt.mapper.ReviewMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewMapper reviewMapper;
    private final ReviewPictureMapper reviewPictureMapper;
    /**
    * 리뷰 생성
    */
    @Transactional
    public int createReview(Review review, List<MultipartFile> images) {

        int result = reviewMapper.insert(review);

        if (images != null && !images.isEmpty()) {

            String uploadDir = System.getProperty("user.dir")
                    + "/src/main/resources/static/upload";

            File directory = new File(uploadDir);
            if (!directory.exists() && !directory.mkdirs()) {
                throw new RuntimeException("업로드 디렉토리 생성 실패");
            }

            for (MultipartFile file : images) {
                if (file.isEmpty()) continue;

                String storeFileName =
                        UUID.randomUUID() + "_" + file.getOriginalFilename();

                File saveFile = new File(uploadDir, storeFileName);

                try {
                    file.transferTo(saveFile);
                    reviewPictureMapper.insertImageUrl(
                            review.getId(), storeFileName
                    );
                } catch (IOException e) {
                    throw new RuntimeException("이미지 저장 실패", e);
                }
            }
        }

        return result;
    }
    /**
     *타겟으로부터 리뷰 조회
     */
    public List<Review> getReviewsByTarget(int targetId, String targetType){ return reviewMapper.getReviewsByTarget(targetId,targetType);}
    /**
     *유저로부터 리뷰 조회
     */
    public List<Review> getReviewsByUserId(int userId){ return reviewMapper.getReviewsByUserId(userId);}
    /**
     *리뷰 업데이트
     */
    public int updateReview(
            Review review,
            List<MultipartFile> newImages,
            List<Long> keepImageIds
    ) {
        int result = reviewMapper.update(review);

        List<ReviewPicture> existingImages =
                reviewPictureMapper.findByReviewId(Long.valueOf(review.getId()));

        String projectPath = System.getProperty("user.dir");
        String uploadDir = projectPath + "/src/main/resources/static/upload";

        for (ReviewPicture img : existingImages) {
            if (keepImageIds == null || !keepImageIds.contains(img.getId())) {
                File file = new File(uploadDir, img.getPicturePath());
                if (file.exists()) file.delete();

                reviewPictureMapper.deleteById(img.getId());
            }
        }

        if (newImages != null && !newImages.isEmpty()) {
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            for (MultipartFile file : newImages) {
                if (file.isEmpty()) continue;

                String storeName =
                        UUID.randomUUID() + "_" + file.getOriginalFilename();

                try {
                    file.transferTo(new File(uploadDir, storeName));
                    reviewPictureMapper.insertImageUrl(review.getId(), storeName);
                } catch (IOException e) {
                    throw new RuntimeException("이미지 저장 실패", e);
                }
            }
        }

        return result;
    }

    /**
     *리뷰 삭제
     */
    public int delete(int reviewId){ return reviewMapper.delete(reviewId);}
    /**
     *평점 조회
     */
    public double getAverageRatingByTarget(int targetId, int targetType){ return reviewMapper.getAverageRatingByTarget(targetId,targetType);}


}