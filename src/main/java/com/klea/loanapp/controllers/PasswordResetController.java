package com.klea.loanapp.controllers;

import com.klea.loanapp.services.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/password")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        passwordResetService.generateResetToken(email);
        return ResponseEntity.ok("Password reset link sent to your email");
    }
}

