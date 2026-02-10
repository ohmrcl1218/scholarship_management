package com.example.scholarship.services;

import com.example.scholarship.models.ScholarshipApplication;
import com.example.scholarship.models.User;
import com.example.scholarship.repo.ApplicationRepository;
import com.example.scholarship.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ScholarshipApplication createApplication(Long userId, ScholarshipApplication application) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check if user already has an application
        Optional<ScholarshipApplication> existingApp = applicationRepository.findByUser(user);
        if (existingApp.isPresent()) {
            throw new RuntimeException("User already has an application");
        }
        
        application.setUser(user);
        application.setApplicationStatus("DRAFT");
        
        return applicationRepository.save(application);
    }

    @Transactional
    public ScholarshipApplication updateApplication(Long applicationId, ScholarshipApplication applicationDetails) {
        ScholarshipApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        
        // Update fields - add all fields that need updating
        if (applicationDetails.getMiddleName() != null) {
            application.setMiddleName(applicationDetails.getMiddleName());
        }
        if (applicationDetails.getBirthDate() != null) {
            application.setBirthDate(applicationDetails.getBirthDate());
        }
        if (applicationDetails.getAge() != null) {
            application.setAge(applicationDetails.getAge());
        }
        // Add other fields as needed...
        
        application.setUpdatedAt(LocalDateTime.now());
        
        return applicationRepository.save(application);
    }

    @Transactional
    public ScholarshipApplication submitApplication(Long applicationId) {
        ScholarshipApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        
        // Validate required fields before submission
        if (application.getEssay() == null || application.getEssay().trim().isEmpty()) {
            throw new RuntimeException("Essay is required for submission");
        }
        
        application.setApplicationStatus("SUBMITTED");
        application.setSubmittedAt(LocalDateTime.now());
        
        return applicationRepository.save(application);
    }

    public ScholarshipApplication getApplicationByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return applicationRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }

    public ScholarshipApplication getApplicationByReference(String referenceNumber) {
        return applicationRepository.findByReferenceNumber(referenceNumber)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }

    public ScholarshipApplication getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }

    public List<ScholarshipApplication> getAllApplications() {
        return applicationRepository.findAll();
    }

    public List<ScholarshipApplication> getApplicationsByStatus(String status) {
        return applicationRepository.findByApplicationStatus(status);
    }

    @Transactional
    public ScholarshipApplication updateApplicationStatus(Long applicationId, String status, String notes) {
        ScholarshipApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        
        application.setApplicationStatus(status);
        if (notes != null) {
            application.setDecisionNotes(notes);
        }
        
        if (status.equals("APPROVED") || status.equals("REJECTED")) {
            application.setFinalDecision(status);
            application.setDecisionNotes(notes);
        }
        
        return applicationRepository.save(application);
    }

    public Long countApplicationsByStatus(String status) {
        return applicationRepository.countByStatus(status);
    }

    public Long countTotalApplications() {
        return applicationRepository.countTotalApplications();
    }
}