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


}
