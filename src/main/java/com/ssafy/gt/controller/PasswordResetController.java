package com.ssafy.gt.controller;

import com.ssafy.gt.dto.request.PasswordChangeRequest;
import com.ssafy.gt.dto.request.PasswordResetRequest;
import com.ssafy.gt.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/password")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    /**
     * 비밀번호 재설정 요청 (이메일로 토큰 전송)
     * POST /api/v1/password/reset-request
     */
    @PostMapping("/reset-request")
    public ResponseEntity<Map<String, Object>> requestPasswordReset(@Valid @RequestBody PasswordResetRequest request) {
        Map<String, Object> response = new HashMap<>();

        int result = passwordResetService.createPasswordResetToken(request.getEmail());

        if (result == 1) {
            response.put("success", true);
            response.put("message", "비밀번호 재설정 이메일이 발송되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "해당 이메일로 등록된 사용자가 없습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 비밀번호 재설정
     * POST /api/v1/password/reset
     */
    @PostMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetPassword(@Valid @RequestBody PasswordChangeRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
            response.put("success", true);
            response.put("message", "비밀번호가 성공적으로 변경되었습니다.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "비밀번호 변경 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 토큰 유효성 검증
     * GET /api/v1/password/verify-token?token={token}
     */
    @GetMapping("/verify-token")
    public ResponseEntity<Map<String, Object>> verifyToken(@RequestParam String token) {
        Map<String, Object> response = new HashMap<>();

        try {
            boolean isValid = passwordResetService.validateToken(token);
            response.put("valid", isValid);
            response.put("message", "유효한 토큰입니다.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("valid", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}

