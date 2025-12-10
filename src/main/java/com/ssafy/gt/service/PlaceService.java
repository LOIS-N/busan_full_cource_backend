package com.ssafy.gt.service;

import com.ssafy.gt.dto.Place;
import com.ssafy.gt.mapper.PlaceMapper;
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
public class PlaceService {

    private final PlaceMapper placeMapper;
    private final LevenshteinDistance levenshtein = new LevenshteinDistance(3);

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
     * 장소 검색
     */
    public List<Place> search(String keyword, Integer tag) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        String lowerKeyword = keyword.toLowerCase();
        List<Place> candidates;

        // 1단계: Full-Text로 정확한 매치 시도
        String searchTerm = "*" + keyword + "*";
        candidates = placeMapper.findCandidates(searchTerm, tag);

        // 2단계: Full-Text 결과가 충분하면 그것만 사용 (빠른 검색)
        if (candidates.size() >= 10) {
            return candidates.stream()
                    .map(place -> {
                        int nameDistance = levenshtein.apply(lowerKeyword, place.getName().toLowerCase());
                        return new SearchResult(place, nameDistance);
                    })
                    .filter(result -> result.distance <= 2)
                    .sorted(Comparator.comparingInt(r -> r.distance))
                    .limit(20)
                    .map(result -> result.place)
                    .collect(Collectors.toList());
        }

        // 3단계: Full-Text 결과가 적으면 전체 검색 (오타 처리)
        candidates = placeMapper.selectAllByTag(tag);

        return candidates.stream()
                .map(place -> {
                    // name에서 거리 계산
                    int nameDistance = levenshtein.apply(lowerKeyword, place.getName().toLowerCase());

                    // address를 단어별로 분리해서 최소 거리 찾기
                    int addressDistance = Integer.MAX_VALUE;
                    if (place.getAddress() != null) {
                        String[] words = place.getAddress().toLowerCase().split("[\\s,]+");
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

                    return new SearchResult(place, finalDistance);
                })
                .filter(result -> result.distance <= 2)  // 거리 2 이내만
                .sorted(Comparator.comparingInt(r -> r.distance))
                .limit(20)
                .map(result -> result.place)
                .collect(Collectors.toList());
    }

    // 내부 클래스
    private static class SearchResult {
        Place place;
        int distance;

        SearchResult(Place place, int distance) {
            this.place = place;
            this.distance = distance;
        }
    }
}