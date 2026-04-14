package com.diana.auditinsightbackendspringboot.modules.auth.controller;

import com.diana.auditinsightbackendspringboot.modules.auth.dto.*;
import com.diana.auditinsightbackendspringboot.modules.auth.entity.User;
import com.diana.auditinsightbackendspringboot.modules.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        try {
            authService.signup(request);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Account created successfully");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(error);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            String token = authService.login(request);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage()); // e.g. "Invalid credentials"
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok("OTP sent to your email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok("Password reset successful.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam Long userId, @RequestParam String code) {
        authService.verifyOtp(userId, code);
        return ResponseEntity.ok("OTP verified successfully.");
    }
}