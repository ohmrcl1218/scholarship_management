package com.example.scholarship.model;

import jakarta.persistence.*;

@Entity
public class Scholarship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;
    private String program;
    private double amount;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
