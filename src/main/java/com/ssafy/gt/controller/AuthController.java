package com.ssafy.gt.controller;

import com.ssafy.gt.dto.response.LoginResponse;
import com.ssafy.gt.dto.request.TokenRefreshRequest;
import com.ssafy.gt.dto.response.TokenRefreshResponse;
import com.ssafy.gt.dto.User;
import com.ssafy.gt.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입
     * POST /api/v1/auth/regist
     */
    @PostMapping("/regist")
    public ResponseEntity<User> regist(@RequestBody User user) {

        return ResponseEntity.ok(authService.regist(user));
    }

    /**
     * 로그인
     * POST /api/v1/auth/login/
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody User user) {
        return ResponseEntity.ok(authService.login(user));
    }

    /**
     * 회원 정보
     * GET /api/v1/auth/getUserInfo?userId={userId}
     */
    @GetMapping("/getUserInfo")
    public ResponseEntity<User> getUserInfo(@RequestParam("userId") String userId) {
        User user = new User();
        user.setUserId(userId);
        user = authService.selectByUserId(user);

        user.setPassword(null);
        user.setId(null);
        return ResponseEntity.ok(user);
    }

    /**
     * 아이디 중복 검사
     * GET /api/v1/auth/checkUserId?userId={userId}
     */
    @GetMapping("/checkUserId")
    public ResponseEntity<Integer> checkUserId(@RequestParam("userId") String userId) {
        User user = new User();
        user.setUserId(userId);
        return ResponseEntity.ok(authService.checkUserId(user));
    }

    /**
     * 이메일 중복 검사
     * GET /api/v1/auth/checkEmail?email={email}
     */
    @GetMapping("/checkEmail")
    public ResponseEntity<Integer> checkEmail(@RequestParam("email") String email) {
        User user = new User();
        user.setEmail(email);
        return ResponseEntity.ok(authService.checkEmail(user));
    }

    /**
     * 회원 정보 업데이트
     * POST /api/v1/auth/userUpdate
     */
    @PostMapping("/userUpdate")
    public ResponseEntity<Integer> update(@RequestBody User user) {
        return ResponseEntity.ok(authService.userUpdate(user));
    }

    /**
     * Access Token 갱신
     * POST /api/v1/auth/refresh
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        TokenRefreshResponse response = authService.refreshAccessToken(request.getRefreshToken());
        if (response == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(response);
    }

    /**
     * 로그아웃
     * POST /api/v1/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() != null) {
            String userId = authentication.getPrincipal().toString();
            authService.logout(userId);
        }
        return ResponseEntity.ok().build();
    }
}
