package com.ssafy.gt.mapper;

import com.ssafy.gt.dto.RestaurantLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RestaurantLogMapper {

    // Insert
    int insert(RestaurantLog restaurantLog);

    // Select by User ID (최근 10개)
    List<RestaurantLog> selectById(@Param("id") int id);
}