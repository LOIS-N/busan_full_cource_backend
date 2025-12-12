package com.ssafy.gt.service;

import com.ssafy.gt.dto.LoginResponse;
import com.ssafy.gt.dto.User;
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
         * 회원가입
         */
        public User regist(User user) {
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);

                authMapper.regist(user);

                user.setPassword(null);

                return user;
        }

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
                        String accessToken = jwtUtil.generateToken(dbUser.getUserId());
                        String refreshToken = refreshTokenService.createRefreshToken(dbUser.getUserId());

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
         * 회원정보 조회
         */
        public User selectByUserId(User user) {
                return authMapper.selectByUserId(user);
        }

        /**
         * 아이디 중복 검사
         */
        public Integer checkUserId(User user) {
                User Checkuser = authMapper.checkUserId(user);
                if(Checkuser != null) { return 1; }
                return 0;
        }

        /**
         * 이메일 중복 검사
         */
        public Integer checkEmail(User user) {
                User Checkuser = authMapper.checkEmail(user);
                if(Checkuser != null) { return 1; }
                return 0;
        }

        /**
         * 회원정보 수정
         */
        public Integer userUpdate(User user) {
                return authMapper.update(user);
        }

        /**
         * AccessToken 갱신
         */
        public com.ssafy.gt.dto.TokenRefreshResponse refreshAccessToken(String refreshToken) {
                if (!refreshTokenService.validateRefreshToken(refreshToken)) {
                        return null;
                }

                String userId = jwtUtil.getUserIdFromToken(refreshToken);
                String newAccessToken = jwtUtil.generateToken(userId);

                return com.ssafy.gt.dto.TokenRefreshResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(refreshToken)
                        .build();
        }

        /**
         * 로그아웃 (RefreshToken 삭제)
         */
        public void logout(String userId) {
                refreshTokenService.deleteByUserId(userId);
        }
}
