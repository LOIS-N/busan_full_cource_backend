package com.ssafy.gt.controller;

import com.ssafy.gt.dto.Place;
import com.ssafy.gt.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @Value("${default.search.distance}")
    private Double defaultSearchDistance;

    /**
     * 거리 기반 장소 검색
     * GET /api/v1/place/getPlaces?x={x}&y={y}&dist={dist}&tag={tag}
     */
    @PostMapping("/getPlaces")
    public ResponseEntity<List<Place>> getPlaces(
            @RequestParam Double x,
            @RequestParam Double y,
            @RequestParam(required = false) Double dist,
            @RequestParam(required = false) Integer tag) {
        double searchDistance = (dist != null) ? dist : defaultSearchDistance;
        List<Place> places = placeService.getPlacesByLocation(x, y, searchDistance, tag);
        return ResponseEntity.ok(places);
    }

    /**
     * ID로 장소 조회
     * GET /api/v1/place/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable int id) {
        Place place = placeService.getPlaceById(id);
        if (place == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(place);
    }

    /**
     * 태그로 장소 조회 (거리 기반)
     * GET /api/v1/place/tag/{tag}?x={x}&y={y}&dist={dist}
     */
    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<Place>> getPlacesByTag(
            @PathVariable int tag,
            @RequestParam Double x,
            @RequestParam Double y,
            @RequestParam(required = false) Double dist) {
        double searchDistance = (dist != null) ? dist : defaultSearchDistance;
        List<Place> places = placeService.getPlacesByLocation(x, y, searchDistance, tag);
        return ResponseEntity.ok(places);
    }

    /**
     * 검색어로 장소 조회 (거리 기반)
     * GET /api/v1/place/search/{search}?x={x}&y={y}&dist={dist}&tag={tag}&userId={userId}
     */
    @GetMapping("/search/{search}")
    public ResponseEntity<List<Place>> search(
            @PathVariable String search,
            @RequestParam Double x,
            @RequestParam Double y,
            @RequestParam(required = false) Double dist,
            @RequestParam(required = false) Integer tag,
            @RequestParam(required = false) Integer userId) {
        double searchDistance = (dist != null) ? dist : defaultSearchDistance;
        List<Place> results = placeService.search(search, x, y, searchDistance, tag, userId);
        return ResponseEntity.ok(results);
    }

    /**
     * 모든 장소 조회
     * GET /api/v1/place
     */
    @GetMapping
    public ResponseEntity<List<Place>> getAllPlaces() {
        List<Place> places = placeService.getAllPlaces();
        return ResponseEntity.ok(places);
    }

    /**
     * 장소 등록
     * POST /api/v1/place
     */
    @PostMapping
    public ResponseEntity<Place> createPlace(@RequestBody Place place) {
        placeService.createPlace(place);
        return ResponseEntity.ok(place);
    }

    /**
     * 장소 수정
     * PUT /api/v1/place/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePlace(
            @PathVariable int id,
            @RequestBody Place place) {
        place.setId(id);
        placeService.updatePlace(place);
        return ResponseEntity.ok().build();
    }

    /**
     * 평균 평점 업데이트
     * PATCH /api/v1/place/{id}/rating
     */
    @PatchMapping("/{id}/rating")
    public ResponseEntity<Void> updateAverageRating(
            @PathVariable int id,
            @RequestParam BigDecimal averageRating) {
        placeService.updateAverageRating(id, averageRating);
        return ResponseEntity.ok().build();
    }

    /**
     * 장소 삭제
     * DELETE /api/v1/place/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable int id) {
        placeService.deletePlace(id);
        return ResponseEntity.ok().build();
    }
}