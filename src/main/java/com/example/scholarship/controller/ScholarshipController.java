package com.example.scholarship.controller;

import com.example.scholarship.model.Scholarship;
import com.example.scholarship.repository.ScholarshipRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scholarships")
public class ScholarshipController {

    private final ScholarshipRepository repository;

    public ScholarshipController(ScholarshipRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Scholarship> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Scholarship addScholarship(@RequestBody Scholarship scholarship) {
        return repository.save(scholarship);
    }

    @DeleteMapping("/{id}")
    public void deleteScholarship(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
