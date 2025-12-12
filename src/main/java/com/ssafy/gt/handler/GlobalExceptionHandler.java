package com.ssafy.gt.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * IllegalArgumentException (아이디 중복 등 비즈니스 로직 예외) 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {

        Map<String, String> errorResponse = Collections.singletonMap("message", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 Conflict (자원 충돌)
                .body(errorResponse);
    }

    /**
     * 이외의 모든 예상치 못한 예외 (DB 오류 등) 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception e) {
        Map<String, String> errorResponse = Collections.singletonMap("message", "처리 중 알 수 없는 서버 오류가 발생했습니다.");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
                .body(errorResponse);
    }
}