package com.ssafy.gt.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimplifiedPlace {
    private Integer id;
    private String name;
    private Integer tag;
    private String tagType;
}