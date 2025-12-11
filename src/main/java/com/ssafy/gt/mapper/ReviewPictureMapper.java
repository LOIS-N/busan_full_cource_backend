package com.ssafy.gt.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReviewPictureMapper {
    int insertImageUrl(int reviewId , String storeFileName);
    int updateImageUrl(int reviewId , String storeFileName);
}
