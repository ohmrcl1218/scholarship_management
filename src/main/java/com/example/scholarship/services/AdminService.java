package com.example.scholarship.services;

import com.example.scholarship.models.Admin;
import com.example.scholarship.models.ScholarshipApplication;
import com.example.scholarship.repo.AdminRepository;
import com.example.scholarship.repo.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationService applicationService;

    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    public Admin getAdminById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    @Transactional
    public Admin updateAdminProfile(Long adminId, Admin adminDetails) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        if (adminDetails.getFullName() != null) {
            admin.setFullName(adminDetails.getFullName());
        }
        if (adminDetails.getRole() != null) {
            admin.setRole(adminDetails.getRole());
        }
        
        admin.setUpdatedAt(LocalDateTime.now());
        
        return adminRepository.save(admin);
    }

    public List<ScholarshipApplication> searchApplications(String searchTerm) {
        return applicationRepository.findByFilters(null, searchTerm);
    }

    public Map<String, Long> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();
        
        stats.put("total", applicationRepository.countTotalApplications());
        stats.put("draft", applicationRepository.countByStatus("DRAFT"));
        stats.put("submitted", applicationRepository.countByStatus("SUBMITTED"));
        stats.put("under_review", applicationRepository.countByStatus("UNDER_REVIEW"));
        stats.put("approved", applicationRepository.countByStatus("APPROVED"));
        stats.put("rejected", applicationRepository.countByStatus("REJECTED"));
        
        return stats;
    }

    public List<ScholarshipApplication> getApplicationsWithFilters(String status, String college) {
        return applicationRepository.findByFilters(status, college);
    }

    @Transactional
    public ScholarshipApplication scheduleInterview(Long applicationId, LocalDateTime interviewDate) {
        ScholarshipApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        
        application.setInterviewDate(interviewDate);
        application.setApplicationStatus("INTERVIEW");
        application.setInterviewStatus("SCHEDULED");
        
        return applicationRepository.save(application);
    }

    @Transactional
    public ScholarshipApplication updateInterviewStatus(Long applicationId, String status, String notes) {
        ScholarshipApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        
        application.setInterviewStatus(status);
        if (notes != null) {
            application.setDecisionNotes(
                (application.getDecisionNotes() != null ? application.getDecisionNotes() + "\n" : "") +
                "Interview: " + notes
            );
        }
        
        return applicationRepository.save(application);
    }
}