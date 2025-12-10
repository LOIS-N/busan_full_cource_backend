package com.ssafy.gt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    // join으로 가져올 데이터
    private String nickname;
    private List<ReviewPicture> pictures;
}
