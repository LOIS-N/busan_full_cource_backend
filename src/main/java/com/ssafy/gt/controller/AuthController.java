package com.ssafy.gt.controller;

import com.ssafy.gt.dto.User;
import com.ssafy.gt.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
     * GET /api/v1/auth/login/
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        return ResponseEntity.ok(authService.login(user));
    }

    /**
     * 아이디 조회
     * GET /api/v1/auth/getUserId?userId={userId}
     */
    @GetMapping("/getUserId")
    public ResponseEntity<User> selectByUserId(@RequestParam("userId") String userId) {
        User user = new User();
        user.setUserId(userId);
        return ResponseEntity.ok(authService.selectByUserId(user));
    }

    /**
     * 이메일 조회
     * GET /api/v1/auth/getEmail?email={email}
     */
    @GetMapping("/getEmail")
    public ResponseEntity<User> selectByEmail(@RequestParam("email") String email) {
        User user = new User();
        user.setEmail(email);
        return ResponseEntity.ok(authService.selectByEmail(user));
    }
}
