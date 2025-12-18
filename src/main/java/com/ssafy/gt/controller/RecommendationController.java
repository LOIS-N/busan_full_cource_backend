package com.ssafy.gt.controller;

import com.ssafy.gt.dto.Place;
import com.ssafy.gt.dto.Restaurant;
import com.ssafy.gt.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/recommendation")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    /**
     * AI 기반 음식점 추천
     * GET /api/v1/recommendation/restaurants?userId={userId}&x={x}&y={y}&dist={dist}&tag={tag}
     */
    @GetMapping("/restaurants")
    public ResponseEntity<List<Restaurant>> recommendRestaurants(
            @RequestParam Integer id,
            @RequestParam Double x,
            @RequestParam Double y,
            @RequestParam(required = false) Double dist,
            @RequestParam(required = false) Integer tag) {

        log.info("AI 음식점 추천 요청 - id: {}, x: {}, y: {}, dist: {}, tag: {}", id, x, y, dist, tag);

        List<Restaurant> recommendations = recommendationService.recommendRestaurants(id, x, y, dist, tag);

        log.info("AI 음식점 추천 결과 - {} 개", recommendations.size());

        return ResponseEntity.ok(recommendations);
    }

    /**
     * AI 기반 장소 추천
     * GET /api/v1/recommendation/places?userId={userId}&x={x}&y={y}&dist={dist}&tag={tag}
     */
    @GetMapping("/places")
    public ResponseEntity<List<Place>> recommendPlaces(
            @RequestParam Integer id,
            @RequestParam Double x,
            @RequestParam Double y,
            @RequestParam(required = false) Double dist,
            @RequestParam(required = false) Integer tag) {

        log.info("AI 장소 추천 요청 - id: {}, x: {}, y: {}, dist: {}, tag: {}", id, x, y, dist, tag);

        List<Place> recommendations = recommendationService.recommendPlaces(id, x, y, dist, tag);

        log.info("AI 장소 추천 결과 - {} 개", recommendations.size());

        return ResponseEntity.ok(recommendations);
    }
}