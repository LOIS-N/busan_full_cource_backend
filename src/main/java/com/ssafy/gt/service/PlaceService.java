package com.ssafy.gt.service;

import com.ssafy.gt.dto.Place;
import com.ssafy.gt.dto.PlaceLog;
import com.ssafy.gt.mapper.PlaceMapper;
import com.ssafy.gt.mapper.PlaceLogMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceMapper placeMapper;
    private final PlaceLogMapper placeLogMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 거리 기반으로 장소 검색
     */
    public List<Place> getPlacesByLocation(Double x, Double y, Double dist, Integer tag) {
        return placeMapper.selectByLocation(x, y, dist, tag);
    }

    /**
     * ID로 장소 조회
     */
    public Place getPlaceById(int id) {
        return placeMapper.selectById(id);
    }

    /**
     * 태그로 장소 조회
     */
    public List<Place> getPlacesByTag(int tag) {
        return placeMapper.selectByTag(tag);
    }

    /**
     * 모든 장소 조회
     */
    public List<Place> getAllPlaces() {
        return placeMapper.selectAll();
    }

    /**
     * 장소 등록
     */
    @Transactional
    public int createPlace(Place place) {
        return placeMapper.insert(place);
    }

    /**
     * 장소 수정
     */
    @Transactional
    public int updatePlace(Place place) {
        return placeMapper.update(place);
    }

    /**
     * 평균 평점 업데이트
     */
    @Transactional
    public int updateAverageRating(int id, BigDecimal averageRating) {
        return placeMapper.updateAverageRating(id, averageRating);
    }

    /**
     * 장소 삭제
     */
    @Transactional
    public int deletePlace(int id) {
        return placeMapper.delete(id);
    }

    /**
     * 장소 검색 (점수 기반 정렬 + 거리 필터링)
     */
    @Transactional
    public List<Place> search(String keyword, Double x, Double y, Double dist, Integer tag, Integer id) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        // 검색 로그 저장 (id가 있을 경우에만)
        if (id != null) {
            saveSearchLog(id, keyword, x, y, dist, tag);
        }

        // Mapper의 점수 기반 정렬 + 거리 필터링 사용
        return placeMapper.findCandidatesWithDistance(keyword, x, y, dist, tag);
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

            PlaceLog log = PlaceLog.builder()
                    .userId(id)
                    .log(logJson)
                    .build();

            placeLogMapper.insert(log);
        } catch (Exception e) {
            // 로그 저장 실패 시 무시 (검색 기능에 영향 없도록)
            e.printStackTrace();
        }
    }
}