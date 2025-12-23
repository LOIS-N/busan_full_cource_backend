package com.ssafy.gt.service;

import com.ssafy.gt.dto.response.LoginResponse;
import com.ssafy.gt.dto.User;
import com.ssafy.gt.dto.response.TokenRefreshResponse;
import com.ssafy.gt.dto.response.UserInfoResponse;
import com.ssafy.gt.mapper.AuthMapper;
import com.ssafy.gt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final AuthMapper authMapper;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtil jwtUtil;
        private final RefreshTokenService refreshTokenService;

        /**
         * 로그인
         */
        public LoginResponse login(User user) {
                User dbUser = authMapper.login(user);

                if (dbUser == null) {
                        return null;
                }

                boolean isPasswordMatch = passwordEncoder.matches(user.getPassword(), dbUser.getPassword());

                if (isPasswordMatch) {
                        String accessToken = jwtUtil.generateToken(dbUser.getId());
                        String refreshToken = refreshTokenService.createRefreshToken(dbUser.getId());
                        UserInfoResponse userInfo = UserInfoResponse.builder()
                                .id(dbUser.getId())
                                .userId(dbUser.getUserId())
                                .nickname(dbUser.getNickname())
                                .email(dbUser.getEmail())
                                .build();

                        dbUser.setPassword(null);
                        return LoginResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .user(dbUser)
                                .build();
                } else {
                        return null;
                }
        }

        /**
         * AccessToken 갱신
         */
        public TokenRefreshResponse refreshAccessToken(String refreshToken) {
                if (!refreshTokenService.validateRefreshToken(refreshToken)) {
                        return null;
                }

                int id = jwtUtil.getUserIdFromToken(refreshToken);
                String newAccessToken = jwtUtil.generateToken(id);

                return TokenRefreshResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(refreshToken)
                        .build();
        }

        /**
         * 로그아웃 (RefreshToken 삭제)
         */
        public void logout(Integer id) {
                refreshTokenService.deleteByUserId(id);
        }
}
