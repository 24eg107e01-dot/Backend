package com.predictor.controller;

import com.predictor.dto.AuthRequest;
import com.predictor.dto.AuthResponse;
import com.predictor.dto.RegisterRequest;
import com.predictor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.ok(Map.of("authenticated", false, "role", "GUEST"));
        }
        return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "email", auth.getName(),
                "authorities", auth.getAuthorities()
        ));
    }
}
