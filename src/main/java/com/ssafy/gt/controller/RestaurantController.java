package com.ssafy.gt.controller;

import com.ssafy.gt.dto.Restaurant;
import com.ssafy.gt.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Value("${default.search.distance}")
    private Double defaultSearchDistance;

    /**
     * 거리 기반 식당 검색
     * GET /api/v1/restaurant/getRestaurants?x={x}&y={y}&dist={dist}&tag={tag}
     */
    @GetMapping("/getRestaurants")
    public ResponseEntity<List<Restaurant>> getRestaurants(
            @RequestParam Double x,
            @RequestParam Double y,
            @RequestParam(required = false) Double dist,
            @RequestParam(required = false) Integer tag) {
        double searchDistance = (dist != null) ? dist : defaultSearchDistance;
        List<Restaurant> restaurants = restaurantService.getRestaurantsByLocation(x, y, searchDistance, tag);
        return ResponseEntity.ok(restaurants);
    }

    /**
     * ID로 식당 조회
     * GET /api/v1/restaurant/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable int id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        if (restaurant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(restaurant);
    }

    /**
     * 태그로 식당 조회
     * GET /api/v1/restaurant/tag/{tag}?x={x}&y={y}&dist={dist}
     */
    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<Restaurant>> getRestaurantsByTag(
            @PathVariable int tag,
            @RequestParam Double x,
            @RequestParam Double y,
            @RequestParam(required = false) Double dist) {
        double searchDistance = (dist != null) ? dist : defaultSearchDistance;
        List<Restaurant> restaurants = restaurantService.getRestaurantsByLocation(x, y, searchDistance, tag);
        return ResponseEntity.ok(restaurants);
    }

    /**
     * 검색어로 식당 조회
     * GET /api/v1/restaurant/search/{search}?x={x}&y={y}&dist={dist}&tag={tag}
     */
    @GetMapping("/search/{search}")
    public ResponseEntity<List<Restaurant>> search(
            @PathVariable String search,
            @RequestParam Double x,
            @RequestParam Double y,
            @RequestParam(required = false) Double dist,
            @RequestParam(required = false) Integer tag) {
        double searchDistance = (dist != null) ? dist : defaultSearchDistance;
        List<Restaurant> results = restaurantService.search(search, x, y, searchDistance, tag);
        return ResponseEntity.ok(results);
    }

    /**
     * 모든 식당 조회
     * GET /api/v1/restaurant
     */
    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }

    /**
     * 식당 등록
     * POST /api/v1/restaurant
     */
    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        restaurantService.createRestaurant(restaurant);
        return ResponseEntity.ok(restaurant);
    }

    /**
     * 식당 수정
     * PUT /api/v1/restaurant/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRestaurant(
            @PathVariable int id,
            @RequestBody Restaurant restaurant) {
        restaurant.setId(id);
        restaurantService.updateRestaurant(restaurant);
        return ResponseEntity.ok().build();
    }

    /**
     * 평균 평점 업데이트
     * PATCH /api/v1/restaurant/{id}/rating
     */
    @PatchMapping("/{id}/rating")
    public ResponseEntity<Void> updateAverageRating(
            @PathVariable int id,
            @RequestParam BigDecimal averageRating) {
        restaurantService.updateAverageRating(id, averageRating);
        return ResponseEntity.ok().build();
    }

    /**
     * 식당 삭제
     * DELETE /api/v1/restaurant/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable int id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok().build();
    }
}