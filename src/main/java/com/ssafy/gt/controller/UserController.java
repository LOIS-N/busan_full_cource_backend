package com.ssafy.gt.controller;

import com.ssafy.gt.dto.User;
import com.ssafy.gt.dto.response.UserResponse;
import com.ssafy.gt.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<User> regist(@Valid @RequestBody User user) {

        return ResponseEntity.ok(userService.regist(user));
    }

    /**
     * 회원 정보
     * GET /api/v1/user/getUserInfo?userId={userId}
     */
    @GetMapping("/getUserInfo")
    public ResponseEntity<UserResponse> getUserInfo(@AuthenticationPrincipal Integer id) {
        User user = userService.selectById(id);

        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * 아이디 중복 검사
     * GET /api/v1/user/checkUserId?userId={userId}
     */
    @GetMapping("/checkUserId")
    public ResponseEntity<Integer> checkUserId(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(userService.checkUserId(userId));
    }

    /**
     * 이메일 중복 검사
     * GET /api/v1/user/checkEmail?email={email}
     */
    @GetMapping("/checkEmail")
    public ResponseEntity<Integer> checkEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.checkEmail(email));
    }

    /**
     * 회원 정보 업데이트
     * POST /api/v1/user/userUpdate
     */
    @PostMapping("/userUpdate")
    public ResponseEntity<Integer> update(
            @AuthenticationPrincipal Integer id,
            @RequestBody User user) {
        user.setId(id);
        return ResponseEntity.ok(userService.userUpdate(user));
    }

}
