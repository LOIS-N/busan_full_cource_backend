package com.ssafy.gt.mapper;

import com.ssafy.gt.domain.Place;
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
    List<Place> selectByLocation(@Param("x") Double x, @Param("y") Double y, @Param("dist") Double dist);

    // Select All
    List<Place> selectAll();

    // Update
    int update(Place place);

    // Update Average Rating
    int updateAverageRating(@Param("id") int id, @Param("averageRating") BigDecimal averageRating);

    // Delete
    int delete(int id);
}