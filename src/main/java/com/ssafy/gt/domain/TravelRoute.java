package com.ssafy.gt.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelRoute {
    private Integer id;
    private Integer userId;
    private String route;
    private LocalDateTime createdAt;
}
