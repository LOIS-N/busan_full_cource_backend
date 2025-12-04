package com.ssafy.gt.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
    private Integer id;
    private Integer tag;
    private Double x;
    private Double y;
    private String name;
    private String address;
    private String tel;
    private String openingHours;
    private String imageUrl;
    private String thumbnailUrl;
    private BigDecimal averageRating;
}
