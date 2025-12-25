package com.ssafy.gt.controller;

import com.ssafy.gt.dto.response.LoginResponse;
import com.ssafy.gt.dto.request.TokenRefreshRequest;
import com.ssafy.gt.dto.response.TokenRefreshResponse;
import com.ssafy.gt.dto.User;
import com.ssafy.gt.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * 로그인
     * POST /api/v1/auth/login/
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody User user) {
        LoginResponse loginResponse = authService.login(user);

        if (loginResponse == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(loginResponse);
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
            Integer id = Integer.valueOf(authentication.getName());
            authService.logout(id);
        }
        return ResponseEntity.ok().build();
    }
}
