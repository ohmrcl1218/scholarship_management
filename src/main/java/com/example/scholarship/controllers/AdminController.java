package com.example.scholarship.controllers;

import com.example.scholarship.models.Admin;
import com.example.scholarship.models.ScholarshipApplication;
import com.example.scholarship.services.AdminService;
import com.example.scholarship.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthService authService;

    @GetMapping("/profile")
    public ResponseEntity<?> getAdminProfile(Authentication authentication) {
        try {
            String email = authentication.getName();
            Admin adminDetails = authService.getAdminByEmail(email);
            return ResponseEntity.ok(adminDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Admin profile not found: " + e.getMessage()));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateAdminProfile(
            Authentication authentication,
            @RequestBody Admin adminDetails) {
        try {
            String email = authentication.getName();
            Admin currentAdmin = authService.getAdminByEmail(email);
            Admin updatedAdmin = adminService.updateAdminProfile(currentAdmin.getId(), adminDetails);
            return ResponseEntity.ok(updatedAdmin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<?> getDashboardStats() {
        try {
            Map<String, Long> stats = adminService.getDashboardStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch dashboard statistics"));
        }
    }

    @GetMapping("/applications")
    public ResponseEntity<?> getAllApplications(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String college,
            @RequestParam(required = false) String search) {
        try {
            List<ScholarshipApplication> applications;
            
            if (search != null && !search.trim().isEmpty()) {
                applications = adminService.searchApplications(search);
            } else {
                applications = adminService.getApplicationsWithFilters(status, college);
            }
            
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch applications"));
        }
    }

    @PostMapping("/applications/{id}/interview")
    public ResponseEntity<?> scheduleInterview(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime interviewDate) {
        try {
            ScholarshipApplication application = adminService.scheduleInterview(id, interviewDate);
            return ResponseEntity.ok(application);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/applications/{id}/interview")
    public ResponseEntity<?> updateInterviewStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String notes) {
        try {
            ScholarshipApplication application = adminService.updateInterviewStatus(id, status, notes);
            return ResponseEntity.ok(application);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/health")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Admin API",
            "timestamp", LocalDateTime.now()
        ));
    }
}