package com.ssafy.gt.mapper;

import com.ssafy.gt.dto.PasswordResetToken;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PasswordResetTokenMapper {

    void insertToken(PasswordResetToken token);

    PasswordResetToken findByToken(String token);

    void deleteByUserId(String userId);

    void markAsUsed(String token);

    void deleteExipredTokens();
}
