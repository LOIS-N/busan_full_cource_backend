package com.ssafy.gt.service;


import com.ssafy.gt.dto.TravelRoute;
import com.ssafy.gt.mapper.TravelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelService {
    private final TravelMapper travelMapper;

    public int createTravelPlan(TravelRoute travelRoute){
        return travelMapper.insertTravelPlan(travelRoute);
    }

    public List<TravelRoute> getTravelPlansByUser(int userId){
        return travelMapper.getTravelPlansByUser(userId);
    }

    public TravelRoute getTravelPlanById(int userId,int id){
        TravelRoute travelRoute = travelMapper.getTravelPlanById(id);
        if(travelRoute == null || ! travelRoute.getUserId().equals(userId)){
            throw new RuntimeException("비정상적인 접근입니다.");
        }
        return travelRoute;
    }

    public int updateTravel(TravelRoute travel){
        return travelMapper.updateTravel(travel);
    }

    public void deleteTravel(int userId, int id){
        TravelRoute travelRoute = travelMapper.getTravelPlanById(id);
        if(travelRoute == null || ! travelRoute.getUserId().equals(userId)){
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
        travelMapper.deleteTravelById(id);
    }
}
