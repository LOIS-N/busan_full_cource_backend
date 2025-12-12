package com.ssafy.gt.mapper;

import com.ssafy.gt.dto.RefreshToken;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RefreshTokenMapper {

    /**
     * RefreshToken 저장 (이미 존재하면 업데이트)
     */
    void save(RefreshToken refreshToken);

    /**
     * Token으로 RefreshToken 조회
     */
    RefreshToken findByToken(String token);

    /**
     * UserId로 RefreshToken 조회
     */
    RefreshToken findByUserId(String userId);

    /**
     * RefreshToken 삭제
     */
    void deleteByUserId(String userId);

    /**
     * 만료된 RefreshToken 삭제
     */
    void deleteExpiredTokens();
}