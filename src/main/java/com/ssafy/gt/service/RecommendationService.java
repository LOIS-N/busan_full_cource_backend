package com.ssafy.gt.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.gt.dto.*;
import com.ssafy.gt.dto.ai.SimplifiedPlace;
import com.ssafy.gt.dto.ai.SimplifiedSearchHistory;
import com.ssafy.gt.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendationService {

    private final RestaurantMapper restaurantMapper;
    private final PlaceMapper placeMapper;
    private final RestaurantLogMapper restaurantLogMapper;
    private final PlaceLogMapper placeLogMapper;
    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    @Value("${default.search.distance}")
    private Double defaultDistance;

    /**
     * AI 기반 음식점 추천
     */
    public List<Restaurant> recommendRestaurants(Integer id, Double x, Double y, Double dist, Integer tag) {
        // 1. 거리 기본값 설정
        if (dist == null) {
            dist = defaultDistance;
        }

        // 2. 사용자의 최근 검색 기록 10개 가져오기
        List<SimplifiedSearchHistory> userHistory = getUserSearchHistory(id, "restaurant");

        // 3. 현재 위치 주변의 후보 음식점 가져오기
        List<Restaurant> candidates = restaurantMapper.selectByLocation(x, y, dist, tag);

        // 후보가 없으면 빈 리스트 반환
        if (candidates == null || candidates.isEmpty()) {
            return Collections.emptyList();
        }

        // 4. 후보 음식점을 SimplifiedPlace로 변환
        List<SimplifiedPlace> simplifiedCandidates = candidates.stream()
                .map(r -> SimplifiedPlace.builder()
                        .id(r.getId())
                        .name(r.getName())
                        .tag(r.getTag())
                        .tagType(r.getTagType())
                        .build()
                )
                .collect(Collectors.toList());

        // 5. AI에게 추천 요청
        List<Integer> recommendedIds = geminiService.recommendPlaces(userHistory, simplifiedCandidates);

        // 6. 추천 순서대로 원본 데이터 정렬
        return sortByRecommendedIds(candidates, recommendedIds);
    }

    /**
     * AI 기반 장소 추천
     */
    public List<Place> recommendPlaces(Integer id, Double x, Double y, Double dist, Integer tag) {
        // 1. 거리 기본값 설정
        if (dist == null) {
            dist = defaultDistance;
        }

        // 2. 사용자의 최근 검색 기록 10개 가져오기
        List<SimplifiedSearchHistory> userHistory = getUserSearchHistory(id, "place");

        // 3. 현재 위치 주변의 후보 장소 가져오기
        List<Place> candidates = placeMapper.selectByLocation(x, y, dist, tag);

        // 후보가 없으면 빈 리스트 반환
        if (candidates == null || candidates.isEmpty()) {
            return Collections.emptyList();
        }

        // 4. 후보 장소를 SimplifiedPlace로 변환
        List<SimplifiedPlace> simplifiedCandidates = candidates.stream()
                .map(p -> SimplifiedPlace.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .tag(p.getTag())
                        .tagType(p.getTagType())
                        .build()
                )
                .collect(Collectors.toList());

        // 5. AI에게 추천 요청
        List<Integer> recommendedIds = geminiService.recommendPlaces(userHistory, simplifiedCandidates);

        // 6. 추천 순서대로 원본 데이터 정렬
        return sortByRecommendedIds(candidates, recommendedIds);
    }

    /**
     * 사용자의 검색 기록을 SimplifiedSearchHistory로 변환
     */
    private List<SimplifiedSearchHistory> getUserSearchHistory(Integer id, String type) {
        List<SimplifiedSearchHistory> history = new ArrayList<>();

        try {
            if ("restaurant".equals(type)) {
                List<RestaurantLog> logs = restaurantLogMapper.selectById(id);
                history = logs.stream()
                        .map(this::parseRestaurantLog)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            } else if ("place".equals(type)) {
                List<PlaceLog> logs = placeLogMapper.selectById(id);
                history = logs.stream()
                        .map(this::parsePlaceLog)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("검색 기록 조회 중 오류 발생", e);
        }

        return history;
    }

    /**
     * RestaurantLog를 SimplifiedSearchHistory로 변환
     */
    private SimplifiedSearchHistory parseRestaurantLog(RestaurantLog logEntry) {
        try {
            Map<String, Object> logData = objectMapper.readValue(logEntry.getLog(), new TypeReference<>() {});
            String keyword = (String) logData.get("keyword");

            // tag로부터 tagType을 조회할 수도 있지만, 일단 키워드만 사용
            return SimplifiedSearchHistory.builder()
                    .searchKeyword(keyword)
                    .tagType(null) // tagType은 로그에 저장되어 있지 않으므로 null
                    .build();
        } catch (Exception e) {
            log.warn("RestaurantLog 파싱 실패: {}", logEntry.getLog(), e);
            return null;
        }
    }

    /**
     * PlaceLog를 SimplifiedSearchHistory로 변환
     */
    private SimplifiedSearchHistory parsePlaceLog(PlaceLog logEntry) {
        try {
            Map<String, Object> logData = objectMapper.readValue(logEntry.getLog(), new TypeReference<>() {});
            String keyword = (String) logData.get("keyword");

            return SimplifiedSearchHistory.builder()
                    .searchKeyword(keyword)
                    .tagType(null)
                    .build();
        } catch (Exception e) {
            log.warn("PlaceLog 파싱 실패: {}", logEntry.getLog(), e);
            return null;
        }
    }

    /**
     * 추천 ID 순서에 따라 원본 데이터를 정렬
     */
    private <T> List<T> sortByRecommendedIds(List<T> candidates, List<Integer> recommendedIds) {
        // ID를 키로 하는 Map 생성
        Map<Integer, T> candidateMap = new HashMap<>();

        for (T candidate : candidates) {
            Integer id = null;
            if (candidate instanceof Restaurant) {
                id = ((Restaurant) candidate).getId();
            } else if (candidate instanceof Place) {
                id = ((Place) candidate).getId();
            }

            if (id != null) {
                candidateMap.put(id, candidate);
            }
        }

        // 추천 순서대로 정렬
        List<T> sorted = new ArrayList<>();
        for (Integer id : recommendedIds) {
            T candidate = candidateMap.get(id);
            if (candidate != null) {
                sorted.add(candidate);
                candidateMap.remove(id); // 처리된 항목 제거
            }
        }

        // AI가 누락한 항목이 있다면 끝에 추가
        sorted.addAll(candidateMap.values());

        return sorted;
    }
}