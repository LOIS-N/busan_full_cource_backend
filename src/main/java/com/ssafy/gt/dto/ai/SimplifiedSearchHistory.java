package com.ssafy.gt.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimplifiedSearchHistory {
    private String searchKeyword;
    private String tagType;
}