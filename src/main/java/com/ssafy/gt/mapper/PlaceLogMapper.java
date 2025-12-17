package com.ssafy.gt.mapper;

import com.ssafy.gt.dto.PlaceLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlaceLogMapper {

    // Insert
    int insert(PlaceLog placeLog);

    // Select by User ID
    List<PlaceLog> selectByUserId(@Param("userId") int userId);
}