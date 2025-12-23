package com.ssafy.gt.service;

import com.ssafy.gt.dto.PasswordResetToken;
import com.ssafy.gt.dto.User;
import com.ssafy.gt.mapper.PasswordResetTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserService userService;
    private final PasswordResetTokenMapper tokenMapper;
    private final EmailService emailService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Transactional
    public int createPasswordResetToken(String email) {
        // 1. 사용자 확인
        User user = new User();
        user.setEmail(email);
        User getUser = userService.selectByEmail(user);
        if (getUser == null) {
            return 0;
        }

        // 2. 기존 토큰 제거
        tokenMapper.deleteByUserId(getUser.getUserId());

        // 3. 토큰 생성
        String token = generateToken();

        // 4. 토큰 객체 생성
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUserId(getUser.getUserId());
        passwordResetToken.setCreatedDate(LocalDateTime.now());
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30)); // 30분 유효
        passwordResetToken.setUsed(false);

        // 5. 토큰 저장
        tokenMapper.insertToken(passwordResetToken);

        // 6. url생성 및 메일 발송
        String url = baseUrl + "?token=" + token;
        emailService.sendPasswordResetEmail(email, url);

        return 1;
    }

    private String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[48];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public boolean validateToken(String token) {
        PasswordResetToken resetToken = tokenMapper.findByToken(token);

        if(resetToken == null) {
            throw new IllegalArgumentException("유효하지 않은 링크입니다.");
        }

        if(resetToken.getUsed()) {
            throw new IllegalArgumentException("이미 사용된 링크입니다.");
        }

        if(resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("링크가 만료되었습니다.");
        }

        return true;
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        validateToken(token);

        PasswordResetToken resetToken = tokenMapper.findByToken(token);
        User user = new User();
        user.setUserId(resetToken.getUserId());
        User getUser = userService.selectByUserId(user);

        userService.updateUserPassword(getUser.getUserId(), newPassword);

        tokenMapper.markAsUsed(token);
    }
}
