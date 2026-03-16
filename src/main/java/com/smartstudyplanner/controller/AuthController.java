package com.smartstudyplanner.controller;

import com.smartstudyplanner.entity.User;
import com.smartstudyplanner.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        try {
            return ResponseEntity.ok(authService.signUp(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}