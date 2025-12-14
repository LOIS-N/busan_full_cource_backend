package com.ssafy.gt.dto;

import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Review {
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
