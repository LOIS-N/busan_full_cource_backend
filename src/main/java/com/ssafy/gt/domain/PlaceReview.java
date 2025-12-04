package com.ssafy.gt.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceReview {
    private Integer id;
    private Integer userId;
    private BigDecimal rating;
    private String content;
    private LocalDateTime createdAt;
    private String targetType;
    private Integer targetId;
}
