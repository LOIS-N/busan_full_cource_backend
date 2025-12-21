package com.ssafy.gt.service;

import com.ssafy.gt.dto.RefreshToken;
import com.ssafy.gt.mapper.RefreshTokenMapper;
import com.ssafy.gt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenMapper refreshTokenMapper;
    private final JwtUtil jwtUtil;

    /**
     * RefreshToken 생성 및 저장
     */
    @Transactional
    public String createRefreshToken(Integer id) {
        String token = jwtUtil.generateRefreshToken(id);

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(id)
                .token(token)
                .expiresAt(convertToLocalDateTime(jwtUtil.getRefreshTokenExpiryDate()))
                .build();

        refreshTokenMapper.save(refreshToken);

        return token;
    }

    /**
     * RefreshToken으로 토큰 정보 조회
     */
    public RefreshToken findByToken(String token) {
        return refreshTokenMapper.findByToken(token);
    }

    /**
     * UserId로 RefreshToken 조회
     */
    public RefreshToken findByUserId(Integer id) {
        return refreshTokenMapper.findByUserId(id);
    }

    /**
     * RefreshToken 검증
     */
    public boolean validateRefreshToken(String token) {
        if (!jwtUtil.validateToken(token)) {
            return false;
        }

        RefreshToken refreshToken = refreshTokenMapper.findByToken(token);
        if (refreshToken == null) {
            return false;
        }

        return refreshToken.getExpiresAt().isAfter(LocalDateTime.now());
    }

    /**
     * RefreshToken 삭제
     */
    @Transactional
    public void deleteByUserId(Integer id) {
        refreshTokenMapper.deleteByUserId(id);
    }

    /**
     * 만료된 RefreshToken 삭제
     */
    @Transactional
    public void deleteExpiredTokens() {
        refreshTokenMapper.deleteExpiredTokens();
    }

    /**
     * Date를 LocalDateTime으로 변환
     */
    private LocalDateTime convertToLocalDateTime(java.util.Date date) {
        return new Timestamp(date.getTime()).toLocalDateTime();
    }
}