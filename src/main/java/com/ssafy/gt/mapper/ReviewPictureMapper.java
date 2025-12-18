package com.ssafy.gt.mapper;

import com.ssafy.gt.dto.ReviewPicture;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewPictureMapper {
    int insertImageUrl(int reviewId , String storeFileName);
    List<ReviewPicture> findByReviewId(int reviewId);
    ReviewPicture findById(int id);
    int deleteById(Integer id);
    int deleteByReviewId(int reviewId);
}
