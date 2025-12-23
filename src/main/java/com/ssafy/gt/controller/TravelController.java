package com.ssafy.gt.controller;

import com.ssafy.gt.dto.TravelRoute;
import com.ssafy.gt.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/vi/travel")
@RequiredArgsConstructor
public class TravelController {

    private final TravelService travelService;

    @PostMapping
    public ResponseEntity<TravelRoute> createTripPlan(
            @RequestBody TravelRoute travelPlan,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer userId = Integer.valueOf(authentication.getName());

        travelPlan.setUserId(userId);
        travelPlan.setCreatedAt(LocalDateTime.now());

        travelService.createTravelPlan(travelPlan);

        return ResponseEntity.ok(travelPlan);
    }

    @GetMapping
    public ResponseEntity<List<TravelRoute>> getMyTripPlans(
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer userId = Integer.valueOf(authentication.getName());

        return ResponseEntity.ok(
                travelService.getTravelPlansByUser(userId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TravelRoute> getTravelPlanById(
            @PathVariable Integer id,
            Authentication authentication
    )
    {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer userId = Integer.valueOf(authentication.getName());
        return ResponseEntity.ok(
                travelService.getTravelPlanById(userId,id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTravel(
            @PathVariable Integer id,
            @RequestBody TravelRoute travel,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer userId = Integer.valueOf(authentication.getName());

        travel.setId(id);
        travel.setUserId(userId);
        travelService.updateTravel(travel);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTravel(
            @PathVariable Integer id,
            Authentication authentication
    ) {
        Integer userId = Integer.valueOf(authentication.getName());
        travelService.deleteTravel(userId,id);
        return ResponseEntity.ok().build();
    }

}
