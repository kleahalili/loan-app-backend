package com.klea.loanapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/check-token")
public class CheckTokenController {
    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Token valid");
    }
}