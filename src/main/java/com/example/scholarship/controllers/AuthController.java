package com.example.scholarship.controllers;

import com.example.scholarship.models.Admin;
import com.example.scholarship.models.User;
import com.example.scholarship.services.AuthService;
import com.example.scholarship.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");

            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Get user details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Generate JWT token
            String token = jwtUtil.generateToken(userDetails);
            
            // Update last login for admin users
            Object principal = authentication.getPrincipal();
            if (principal instanceof Admin) {
                authService.updateLastLogin((Admin) principal);
            } else if (principal instanceof User) {
                authService.updateLastLogin((User) principal);
            }
            
            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", Map.of(
                "email", userDetails.getUsername(),
                "role", userDetails.getAuthorities().iterator().next().getAuthority(),
                "enabled", userDetails.isEnabled()
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = authService.registerUser(user);
            
            // Generate token for auto-login
            String token = jwtUtil.generateToken(registeredUser);
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", Map.of(
                "id", registeredUser.getId(),
                "email", registeredUser.getEmail(),
                "firstName", registeredUser.getFirstName(),
                "lastName", registeredUser.getLastName(),
                "role", registeredUser.getUserRole(),
                "createdAt", registeredUser.getCreatedAt()
            ));
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/admin/register")
    public ResponseEntity<?> registerAdmin(@RequestBody Admin admin) {
        try {
            Admin registeredAdmin = authService.registerAdmin(admin);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Admin registered successfully");
            response.put("admin", Map.of(
                "id", registeredAdmin.getId(),
                "email", registeredAdmin.getEmail(),
                "fullName", registeredAdmin.getFullName(),
                "role", registeredAdmin.getRole()
            ));
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Not authenticated"));
            }
            
            String email = authentication.getName();
            UserDetails userDetails = authService.loadUserByUsername(email);
            
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("email", userDetails.getUsername());
            userInfo.put("authorities", userDetails.getAuthorities());
            userInfo.put("enabled", userDetails.isEnabled());
            
            return ResponseEntity.ok(userInfo);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch user info: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        
        return ResponseEntity.ok(Map.of(
            "message", "Logged out successfully",
            "timestamp", LocalDateTime.now()
        ));
    }

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Authentication API",
            "timestamp", LocalDateTime.now()
        ));
    }
}