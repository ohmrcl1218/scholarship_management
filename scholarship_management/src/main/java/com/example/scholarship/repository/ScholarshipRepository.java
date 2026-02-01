package com.example.scholarship.repository;

import com.example.scholarship.model.Scholarship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScholarshipRepository extends JpaRepository<Scholarship, Long> {
}
