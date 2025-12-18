package com.ssafy.gt.service;

import com.ssafy.gt.dto.Restaurant;
import com.ssafy.gt.dto.RestaurantLog;
import com.ssafy.gt.mapper.RestaurantMapper;
import com.ssafy.gt.mapper.RestaurantLogMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantMapper restaurantMapper;
    private final RestaurantLogMapper restaurantLogMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LevenshteinDistance levenshtein = new LevenshteinDistance(3);

    /**
     * 거리 기반으로 식당 검색
     */
    public List<Restaurant> getRestaurantsByLocation(Double x, Double y, Double dist, Integer tag) {
        return restaurantMapper.selectByLocation(x, y, dist, tag);
    }

    /**
     * ID로 식당 조회
     */
    public Restaurant getRestaurantById(int id) {
        return restaurantMapper.selectById(id);
    }

    /**
     * 태그로 식당 조회
     */
    public List<Restaurant> getRestaurantsByTag(int tag) {
        return restaurantMapper.selectByTag(tag);
    }

    /**
     * 모든 식당 조회
     */
    public List<Restaurant> getAllRestaurants() {
        return restaurantMapper.selectAll();
    }

    /**
     * 식당 등록
     */
    @Transactional
    public int createRestaurant(Restaurant restaurant) {
        return restaurantMapper.insert(restaurant);
    }

    /**
     * 식당 수정
     */
    @Transactional
    public int updateRestaurant(Restaurant restaurant) {
        return restaurantMapper.update(restaurant);
    }

    /**
     * 평균 평점 업데이트
     */
    @Transactional
    public int updateAverageRating(int id, BigDecimal averageRating) {
        return restaurantMapper.updateAverageRating(id, averageRating);
    }

    /**
     * 식당 삭제
     */
    @Transactional
    public int deleteRestaurant(int id) {
        return restaurantMapper.delete(id);
    }

    /**
     * 식당 검색 (점수 기반 정렬 + 거리 필터링)
     */
    @Transactional
    public List<Restaurant> search(String keyword, Double x, Double y, Double dist, Integer tag, Integer id) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        // 검색 로그 저장 (id가 있을 경우에만)
        if (id != null) {
            saveSearchLog(id, keyword, x, y, dist, tag);
        }

        // Mapper의 점수 기반 정렬 + 거리 필터링 사용
        return restaurantMapper.findCandidatesWithDistance(keyword, x, y, dist, tag);
    }

    /**
     * 검색 로그 저장
     */
    private void saveSearchLog(Integer id, String keyword, Double x, Double y, Double dist, Integer tag) {
        try {
            Map<String, Object> logData = new HashMap<>();
            logData.put("keyword", keyword);
            logData.put("x", x);
            logData.put("y", y);
            logData.put("dist", dist);
            logData.put("tag", tag);

            String logJson = objectMapper.writeValueAsString(logData);

            RestaurantLog log = RestaurantLog.builder()
                    .userId(id)
                    .log(logJson)
                    .build();

            restaurantLogMapper.insert(log);
        } catch (Exception e) {
            // 로그 저장 실패 시 무시 (검색 기능에 영향 없도록)
            e.printStackTrace();
        }
    }
}