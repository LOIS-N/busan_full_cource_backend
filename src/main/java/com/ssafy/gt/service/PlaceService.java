package com.ssafy.gt.service;

import com.ssafy.gt.domain.Place;
import com.ssafy.gt.mapper.PlaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceMapper placeMapper;

    /**
     * 거리 기반으로 장소 검색
     */
    public List<Place> getPlacesByLocation(Double x, Double y, Double dist) {
        return placeMapper.selectByLocation(x, y, dist);
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
}