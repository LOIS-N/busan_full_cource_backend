package com.ssafy.gt.service;

import com.ssafy.gt.dto.Review;
import com.ssafy.gt.mapper.ReviewPictureMapper;
import com.ssafy.gt.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
//@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewMapper reviewMapper;
    private final ReviewPictureMapper reviewPictureMapper;
    /**
    * 리뷰 생성
    */
    public int createReview(Review review, List<MultipartFile> images){

        int result =  0; //reviewMapper.insert(review);

        if(images != null && !images.isEmpty()){
            String projectPath = System.getProperty("user.dir");
            String uploadDir = projectPath + "/src/main/resources/static/upload";

            File directory = new File(uploadDir);
            if (!directory.exists()) {
                boolean wasSuccessful = directory.mkdirs();
                if (!wasSuccessful) {
                    System.out.println("디렉토리 생성 실패: " + uploadDir);
                }
            }

            for(MultipartFile file : images){
                if(file.isEmpty()) continue;

                String originalName = file.getOriginalFilename();
                String storeFileName = UUID.randomUUID() + "_" + originalName;
                File saveFile = new File(uploadDir, storeFileName);

                try {
                    file.transferTo(saveFile);
                    System.out.println("이미지 저장 성공: " + saveFile.getAbsolutePath());
                    reviewPictureMapper.insertImageUrl(review.getId(), storeFileName);

                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("이미지 저장 중 오류 발생", e);
                }
            }
        }
        return result;
    }
    /**
     *타겟으로부터 리뷰 조회
     */
    public List<Review> getReviewsByTarget(int targetId, int targetType){ return reviewMapper.getReviewsByTarget(targetId,targetType);}
    /**
     *유저로부터 리뷰 조회
     */
    public List<Review> getReviewsByUserId(int userId){ return reviewMapper.getReviewsByUserId(userId);}
    /**
     *리뷰 업데이트
     */
    public int updateReview(Review review,
                            List<MultipartFile> newImages,
                            List<String> deleteImageNames) {
        int result = reviewMapper.update(review);

        if (deleteImageNames != null && !deleteImageNames.isEmpty()) {
            String projectPath = System.getProperty("user.dir");
            String uploadDir = projectPath + "/src/main/resources/static/upload";

            for (String fileName : deleteImageNames) {
                // DB 삭제
                //reviewPictureMapper.deleteImageUrl(review.getId(), fileName);

                File target = new File(uploadDir, fileName);
                if (target.exists()) {
                    target.delete();
                }
            }
        }

        if (newImages != null && !newImages.isEmpty()) {
            String projectPath = System.getProperty("user.dir");
            String uploadDir = projectPath + "/src/main/resources/static/upload";

            File directory = new File(uploadDir);
            if (!directory.exists()) directory.mkdirs();

            for (MultipartFile file : newImages) {
                if (file.isEmpty()) continue;

                String originalName = file.getOriginalFilename();
                String storeFileName = UUID.randomUUID() + "_" + originalName;

                File saveFile = new File(uploadDir, storeFileName);

                try {
                    file.transferTo(saveFile);
                    reviewPictureMapper.insertImageUrl(review.getId(), storeFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("이미지 저장 중 오류 발생", e);
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