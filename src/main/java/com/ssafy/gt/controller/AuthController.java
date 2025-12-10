package com.ssafy.gt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/Auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
}
