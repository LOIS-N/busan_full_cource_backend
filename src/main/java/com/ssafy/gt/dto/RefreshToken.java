package com.ssafy.gt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    private Integer id;
    private String userId;
    private String token;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
}