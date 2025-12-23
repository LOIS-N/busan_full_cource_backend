package com.ssafy.gt.controller;

import com.ssafy.gt.dto.User;
import com.ssafy.gt.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     * POST /api/v1/user/regist
     */
    @PostMapping("/regist")
    public ResponseEntity<User> regist(@RequestBody User user) {

        return ResponseEntity.ok(userService.regist(user));
    }

    /**
     * 회원 정보
     * GET /api/v1/user/getUserInfo?userId={userId}
     */
    @GetMapping("/getUserInfo")
    public ResponseEntity<User> getUserInfo(@RequestParam("userId") String userId) {
        User user = new User();
        user.setUserId(userId);
        user = userService.selectByUserId(user);

        user.setPassword(null);
        user.setId(null);
        return ResponseEntity.ok(user);
    }

    /**
     * 아이디 중복 검사
     * GET /api/v1/user/checkUserId?userId={userId}
     */
    @GetMapping("/checkUserId")
    public ResponseEntity<Integer> checkUserId(@RequestParam("userId") String userId) {
        User user = new User();
        user.setUserId(userId);
        return ResponseEntity.ok(userService.checkUserId(user));
    }

    /**
     * 이메일 중복 검사
     * GET /api/v1/user/checkEmail?email={email}
     */
    @GetMapping("/checkEmail")
    public ResponseEntity<Integer> checkEmail(@RequestParam("email") String email) {
        User user = new User();
        user.setEmail(email);
        return ResponseEntity.ok(userService.checkEmail(user));
    }

    /**
     * 회원 정보 업데이트
     * POST /api/v1/user/userUpdate
     */
    @PostMapping("/userUpdate")
    public ResponseEntity<Integer> update(@RequestBody User user) {
        return ResponseEntity.ok(userService.userUpdate(user));
    }

}
