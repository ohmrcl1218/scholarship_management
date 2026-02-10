package com.example.scholarship.controllers;

import com.example.scholarship.models.ScholarshipApplication;
import com.example.scholarship.models.User;
import com.example.scholarship.services.ApplicationService;
import com.example.scholarship.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/application")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private AuthService authService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createApplication(
            Authentication authentication,
            @RequestBody ScholarshipApplication application) {
        try {
            String email = authentication.getName();
            User user = authService.getUserByEmail(email);
            ScholarshipApplication createdApp = applicationService.createApplication(user.getId(), application);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdApp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/my-application")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getMyApplication(Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = authService.getUserByEmail(email);
            ScholarshipApplication application = applicationService.getApplicationByUserId(user.getId());
            return ResponseEntity.ok(application);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Application not found"));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateApplication(
            @PathVariable Long id,
            Authentication authentication,
            @RequestBody ScholarshipApplication applicationDetails) {
        try {
            String email = authentication.getName();
            User user = authService.getUserByEmail(email);
            
            // Verify the application belongs to the user
            ScholarshipApplication existingApp = applicationService.getApplicationByUserId(user.getId());
            if (!existingApp.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Access denied"));
            }
            
            ScholarshipApplication updatedApp = applicationService.updateApplication(id, applicationDetails);
            return ResponseEntity.ok(updatedApp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/submit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> submitApplication(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = authService.getUserByEmail(email);
            
            // Verify the application belongs to the user
            ScholarshipApplication existingApp = applicationService.getApplicationByUserId(user.getId());
            if (!existingApp.getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Access denied"));
            }
            
            ScholarshipApplication submittedApp = applicationService.submitApplication(id);
            return ResponseEntity.ok(submittedApp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getApplicationsByStatus(@PathVariable String status) {
        try {
            List<ScholarshipApplication> applications = applicationService.getApplicationsByStatus(status);
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch applications"));
        }
    }

    @GetMapping("/reference/{referenceNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getApplicationByReference(@PathVariable String referenceNumber) {
        try {
            ScholarshipApplication application = applicationService.getApplicationByReference(referenceNumber);
            return ResponseEntity.ok(application);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Application not found"));
        }
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateApplicationStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String notes) {
        try {
            ScholarshipApplication application = applicationService.updateApplicationStatus(id, status, notes);
            return ResponseEntity.ok(application);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getApplicationStats() {
        try {
            Map<String, Long> stats = Map.of(
                "total", applicationService.countTotalApplications(),
                "draft", applicationService.countApplicationsByStatus("DRAFT"),
                "submitted", applicationService.countApplicationsByStatus("SUBMITTED"),
                "under_review", applicationService.countApplicationsByStatus("UNDER_REVIEW"),
                "approved", applicationService.countApplicationsByStatus("APPROVED"),
                "rejected", applicationService.countApplicationsByStatus("REJECTED")
            );
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch statistics"));
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllApplications() {
        try {
            List<ScholarshipApplication> applications = applicationService.getAllApplications();
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch applications"));
        }
    }
}