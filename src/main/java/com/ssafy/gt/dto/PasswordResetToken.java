package com.ssafy.gt.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PasswordResetToken {
    private Integer id;
    private String userId;
    private String token;
    private LocalDateTime expiryDate;
    private LocalDateTime createdDate;
    private Boolean used;
}
