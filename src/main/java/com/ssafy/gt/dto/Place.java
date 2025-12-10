package com.ssafy.gt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place {
    private Integer id;
    private Integer tag;
    private Double x;
    private Double y;
    private String name;
    private String address;
    private String imageUrl;
    private String thumbnailUrl;
    private BigDecimal averageRating;

    // join으로 가져올 데이터
    private String tagType;
}
