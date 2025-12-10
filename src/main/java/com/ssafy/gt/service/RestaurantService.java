package com.ssafy.gt.service;

import com.ssafy.gt.dto.Restaurant;
import com.ssafy.gt.mapper.RestaurantMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantMapper restaurantMapper;
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
     * 식당 검색
     */
    public List<Restaurant> search(String keyword, Integer tag) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        String lowerKeyword = keyword.toLowerCase();
        List<Restaurant> candidates;

        // 1단계: Full-Text로 정확한 매치 시도
        String searchTerm = "*" + keyword + "*";
        candidates = restaurantMapper.findCandidates(searchTerm, tag);

        // 2단계: Full-Text 결과가 충분하면 그것만 사용 (빠른 검색)
        if (candidates.size() >= 10) {
            return candidates.stream()
                    .map(restaurant -> {
                        int nameDistance = levenshtein.apply(lowerKeyword, restaurant.getName().toLowerCase());
                        return new SearchResult(restaurant, nameDistance);
                    })
                    .filter(result -> result.distance <= 2)
                    .sorted(Comparator.comparingInt(r -> r.distance))
                    .limit(20)
                    .map(result -> result.restaurant)
                    .collect(Collectors.toList());
        }

        // 3단계: Full-Text 결과가 적으면 전체 검색 (오타 처리)
        candidates = restaurantMapper.selectAllByTag(tag);

        return candidates.stream()
                .map(restaurant -> {
                    // name에서 거리 계산
                    int nameDistance = levenshtein.apply(lowerKeyword, restaurant.getName().toLowerCase());

                    // address를 단어별로 분리해서 최소 거리 찾기
                    int addressDistance = Integer.MAX_VALUE;
                    if (restaurant.getAddress() != null) {
                        String[] words = restaurant.getAddress().toLowerCase().split("[\\s,]+");
                        for (String word : words) {
                            int dist = levenshtein.apply(lowerKeyword, word);
                            if (dist != -1 && dist < addressDistance) {
                                addressDistance = dist;
                            }
                        }
                    }

                    // 더 가까운 거리 선택 (name 또는 address 중)
                    int finalDistance = Math.min(
                            nameDistance != -1 ? nameDistance : Integer.MAX_VALUE,
                            addressDistance
                    );

                    return new SearchResult(restaurant, finalDistance);
                })
                .filter(result -> result.distance <= 2)  // 거리 2 이내만
                .sorted(Comparator.comparingInt(r -> r.distance))
                .limit(20)
                .map(result -> result.restaurant)
                .collect(Collectors.toList());
    }

    // 내부 클래스
    private static class SearchResult {
        Restaurant restaurant;
        int distance;

        SearchResult(Restaurant restaurant, int distance) {
            this.restaurant = restaurant;
            this.distance = distance;
        }
    }
}