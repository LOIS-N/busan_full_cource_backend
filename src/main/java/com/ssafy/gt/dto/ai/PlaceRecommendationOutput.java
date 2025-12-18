package com.ssafy.gt.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceRecommendationOutput {
    @JsonProperty("recommended_ids")
    private List<Integer> recommendedIds;
}