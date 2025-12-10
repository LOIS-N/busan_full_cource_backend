package com.ssafy.gt.mapper;

import com.ssafy.gt.dto.Place;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface PlaceMapper {

    // Insert
    int insert(Place place);

    // Select by ID
    Place selectById(int id);

    // Select by Tag
    List<Place> selectByTag(int tag);

    // Select by Location (거리 기반 검색)
    List<Place> selectByLocation(@Param("x") Double x, @Param("y") Double y, @Param("dist") Double dist, @Param("tag") Integer tag);

    // full-text로 후보군 추출
    List<Place> findCandidates(@Param("keyword") String keyword, @Param("tag") Integer tag);

    // Select All
    List<Place> selectAll();

    // Select All by Tag (tag가 null이면 전체 조회)
    List<Place> selectAllByTag(@Param("tag") Integer tag);

    // Update
    int update(Place place);

    // Update Average Rating
    int updateAverageRating(@Param("id") int id, @Param("averageRating") BigDecimal averageRating);

    // Delete
    int delete(int id);


}