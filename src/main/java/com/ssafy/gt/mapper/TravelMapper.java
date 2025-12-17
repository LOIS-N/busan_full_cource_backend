package com.ssafy.gt.mapper;

import com.ssafy.gt.dto.TravelRoute;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TravelMapper {
    public int insertTravelPlan(TravelRoute travelRoute);
    List<TravelRoute> getTravelPlansByUser(int userId);
    TravelRoute getTravelPlanById(int id);
}
