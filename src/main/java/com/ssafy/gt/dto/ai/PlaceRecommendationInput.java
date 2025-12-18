package com.ssafy.gt.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceRecommendationInput {
    private List<SimplifiedSearchHistory> userHistory;
    private List<SimplifiedPlace> candidates;
}