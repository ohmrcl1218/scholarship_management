package com.example.scholarship.repo;

import com.example.scholarship.models.ScholarshipApplication;
import com.example.scholarship.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<ScholarshipApplication, Long> {
    
    Optional<ScholarshipApplication> findByReferenceNumber(String referenceNumber);
    
    Optional<ScholarshipApplication> findByUser(User user);
    
    List<ScholarshipApplication> findByApplicationStatus(String status);
    
    @Query("SELECT a FROM ScholarshipApplication a WHERE " +
           "(:status IS NULL OR a.applicationStatus = :status) AND " +
           "(:college IS NULL OR a.firstCollegeChoice LIKE %:college% OR a.secondCollegeChoice LIKE %:college% OR a.thirdCollegeChoice LIKE %:college%)")
    List<ScholarshipApplication> findByFilters(@Param("status") String status, 
                                               @Param("college") String college);
    
    @Query("SELECT COUNT(a) FROM ScholarshipApplication a WHERE a.applicationStatus = :status")
    Long countByStatus(@Param("status") String status);
    
    @Query("SELECT COUNT(a) FROM ScholarshipApplication a")
    Long countTotalApplications();
}