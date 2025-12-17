package com.ssafy.gt.mapper;

import com.ssafy.gt.dto.Restaurant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface RestaurantMapper {

    // Insert
    int insert(Restaurant restaurant);

    // Select by ID
    Restaurant selectById(int id);

    // Select by Tag
    List<Restaurant> selectByTag(int tag);

    // Select by Location (거리 기반 검색)
    List<Restaurant> selectByLocation(
            @Param("x") Double x, 
            @Param("y") Double y, 
            @Param("dist") Double dist, 
            @Param("tag") Integer tag);

    // full-text로 후보군 추출
    List<Restaurant> findCandidates(@Param("keyword") String keyword, @Param("tag") Integer tag);

    // 검색 + 거리 필터링
    List<Restaurant> findCandidatesWithDistance(
            @Param("keyword") String keyword,
            @Param("x") Double x,
            @Param("y") Double y,
            @Param("dist") Double dist,
            @Param("tag") Integer tag);

    // Select All
    List<Restaurant> selectAll();

    // Select All by Tag (tag가 null이면 전체 조회)
    List<Restaurant> selectAllByTag(@Param("tag") Integer tag);

    // Update
    int update(Restaurant restaurant);

    // Update Average Rating
    int updateAverageRating(@Param("id") int id, @Param("averageRating") BigDecimal averageRating);

    // Delete
    int delete(int id);

}