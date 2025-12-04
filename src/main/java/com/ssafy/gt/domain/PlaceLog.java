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
public class PlaceLog {
    private Integer id;
    private Integer userId;
    private String log;
    private LocalDateTime createdAt;
}
