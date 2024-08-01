package com.liveCode.todoList.controller;

import com.liveCode.todoList.service.AuthService;
import com.liveCode.todoList.utils.dto.AuthDTO;
import com.liveCode.todoList.utils.dto.RefreshTokenDTO;
import com.liveCode.todoList.utils.dto.RegisterDTO;
import com.liveCode.todoList.utils.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDTO req) {
        log.info("Login request: {}", req.getEmail());
        return Response.renderJSON(
                authService.login(req)
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO req) {
        return Response.renderJSON(
                authService.registerUser(req),
                "Success",
                HttpStatus.CREATED
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDTO request) {
        return ResponseEntity.ok(authService.refreshToken(request.getRefreshToken()));
    }
}
