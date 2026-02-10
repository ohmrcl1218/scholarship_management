package com.example.scholarship.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "scholarship_applications")
public class ScholarshipApplication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "reference_number", unique = true, nullable = false)
    private String referenceNumber;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    private User user;
    
    // Personal Information
    @Column(name = "middle_name")
    private String middleName;
    
    @Column(name = "birth_date")
    private LocalDateTime birthDate;
    
    @Column
    private Integer age;
    
    @Column(name = "place_of_birth")
    private String placeOfBirth;
    
    @Column
    private String sex;
    
    @Column
    private Double height;
    
    @Column
    private Double weight;
    
    @Column(name = "facebook_url")
    private String facebookUrl;
    
    // Address Information
    @Column(name = "house_number")
    private String houseNumber;
    
    @Column
    private String street;
    
    @Column
    private String barangay;
    
    @Column
    private String municipality;
    
    @Column
    private String province;
    
    @Column
    private String region;
    
    @Column(name = "zip_code")
    private String zipCode;
    
    // Academic Information
    @Column(name = "jhs_name")
    private String jhsName;
    
    @Column(name = "jhs_school_id")
    private String jhsSchoolId;
    
    @Column(name = "jhs_type")
    private String jhsType;
    
    @Column(name = "shs_name")
    private String shsName;
    
    @Column(name = "shs_school_id")
    private String shsSchoolId;
    
    @Column(name = "shs_type")
    private String shsType;
    
    @Column
    private String track;
    
    @Column
    private String strand;
    
    @Column(name = "grade_12_gwa")
    private Double grade12Gwa;
    
    @Column(name = "honors_received")
    private String honorsReceived;
    
    // College & Program Choices
    @Column(name = "first_college_choice")
    private String firstCollegeChoice;
    
    @Column(name = "second_college_choice")
    private String secondCollegeChoice;
    
    @Column(name = "third_college_choice")
    private String thirdCollegeChoice;
    
    @Column(name = "first_program_choice")
    private String firstProgramChoice;
    
    @Column(name = "second_program_choice")
    private String secondProgramChoice;
    
    @Column(name = "third_program_choice")
    private String thirdProgramChoice;
    
    // Essay
    @Column(columnDefinition = "TEXT")
    private String essay;
    
    @Column(name = "essay_word_count")
    private Integer essayWordCount;
    
    // Application Status
    @Column(name = "application_status")
    private String applicationStatus = "DRAFT"; // DRAFT, SUBMITTED, UNDER_REVIEW, INTERVIEW, APPROVED, REJECTED
    
    @Column(name = "review_status")
    private String reviewStatus = "PENDING";
    
    @Column(name = "interview_date")
    private LocalDateTime interviewDate;
    
    @Column(name = "interview_status")
    private String interviewStatus;
    
    @Column(name = "final_decision")
    private String finalDecision;
    
    @Column(name = "decision_notes", columnDefinition = "TEXT")
    private String decisionNotes;
    
    // Documents (stored as JSON string or separate table in real implementation)
    @Column(name = "documents_json", columnDefinition = "TEXT")
    private String documentsJson;
    
    // Timestamps
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public ScholarshipApplication() {}
    
    public ScholarshipApplication(Long id, String referenceNumber, User user, String middleName, 
                                  LocalDateTime birthDate, Integer age, String placeOfBirth, 
                                  String sex, Double height, Double weight, String facebookUrl,
                                  String houseNumber, String street, String barangay, 
                                  String municipality, String province, String region, 
                                  String zipCode, String jhsName, String jhsSchoolId, 
                                  String jhsType, String shsName, String shsSchoolId, 
                                  String shsType, String track, String strand, Double grade12Gwa, 
                                  String honorsReceived, String firstCollegeChoice, 
                                  String secondCollegeChoice, String thirdCollegeChoice, 
                                  String firstProgramChoice, String secondProgramChoice, 
                                  String thirdProgramChoice, String essay, Integer essayWordCount, 
                                  String applicationStatus, String reviewStatus, 
                                  LocalDateTime interviewDate, String interviewStatus, 
                                  String finalDecision, String decisionNotes, String documentsJson, 
                                  LocalDateTime submittedAt, LocalDateTime createdAt, 
                                  LocalDateTime updatedAt) {
        this.id = id;
        this.referenceNumber = referenceNumber;
        this.user = user;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.age = age;
        this.placeOfBirth = placeOfBirth;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
        this.facebookUrl = facebookUrl;
        this.houseNumber = houseNumber;
        this.street = street;
        this.barangay = barangay;
        this.municipality = municipality;
        this.province = province;
        this.region = region;
        this.zipCode = zipCode;
        this.jhsName = jhsName;
        this.jhsSchoolId = jhsSchoolId;
        this.jhsType = jhsType;
        this.shsName = shsName;
        this.shsSchoolId = shsSchoolId;
        this.shsType = shsType;
        this.track = track;
        this.strand = strand;
        this.grade12Gwa = grade12Gwa;
        this.honorsReceived = honorsReceived;
        this.firstCollegeChoice = firstCollegeChoice;
        this.secondCollegeChoice = secondCollegeChoice;
        this.thirdCollegeChoice = thirdCollegeChoice;
        this.firstProgramChoice = firstProgramChoice;
        this.secondProgramChoice = secondProgramChoice;
        this.thirdProgramChoice = thirdProgramChoice;
        this.essay = essay;
        this.essayWordCount = essayWordCount;
        this.applicationStatus = applicationStatus;
        this.reviewStatus = reviewStatus;
        this.interviewDate = interviewDate;
        this.interviewStatus = interviewStatus;
        this.finalDecision = finalDecision;
        this.decisionNotes = decisionNotes;
        this.documentsJson = documentsJson;
        this.submittedAt = submittedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getReferenceNumber() {
        return referenceNumber;
    }
    
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getMiddleName() {
        return middleName;
    }
    
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    
    public LocalDateTime getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }
    
    public String getPlaceOfBirth() {
        return placeOfBirth;
    }
    
    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }
    
    public String getSex() {
        return sex;
    }
    
    public void setSex(String sex) {
        this.sex = sex;
    }
    
    public Double getHeight() {
        return height;
    }
    
    public void setHeight(Double height) {
        this.height = height;
    }
    
    public Double getWeight() {
        return weight;
    }
    
    public void setWeight(Double weight) {
        this.weight = weight;
    }
    
    public String getFacebookUrl() {
        return facebookUrl;
    }
    
    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }
    
    public String getHouseNumber() {
        return houseNumber;
    }
    
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
    
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
    
    public String getBarangay() {
        return barangay;
    }
    
    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }
    
    public String getMunicipality() {
        return municipality;
    }
    
    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }
    
    public String getProvince() {
        return province;
    }
    
    public void setProvince(String province) {
        this.province = province;
    }
    
    public String getRegion() {
        return region;
    }
    
    public void setRegion(String region) {
        this.region = region;
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    public String getJhsName() {
        return jhsName;
    }
    
    public void setJhsName(String jhsName) {
        this.jhsName = jhsName;
    }
    
    public String getJhsSchoolId() {
        return jhsSchoolId;
    }
    
    public void setJhsSchoolId(String jhsSchoolId) {
        this.jhsSchoolId = jhsSchoolId;
    }
    
    public String getJhsType() {
        return jhsType;
    }
    
    public void setJhsType(String jhsType) {
        this.jhsType = jhsType;
    }
    
    public String getShsName() {
        return shsName;
    }
    
    public void setShsName(String shsName) {
        this.shsName = shsName;
    }
    
    public String getShsSchoolId() {
        return shsSchoolId;
    }
    
    public void setShsSchoolId(String shsSchoolId) {
        this.shsSchoolId = shsSchoolId;
    }
    
    public String getShsType() {
        return shsType;
    }
    
    public void setShsType(String shsType) {
        this.shsType = shsType;
    }
    
    public String getTrack() {
        return track;
    }
    
    public void setTrack(String track) {
        this.track = track;
    }
    
    public String getStrand() {
        return strand;
    }
    
    public void setStrand(String strand) {
        this.strand = strand;
    }
    
    public Double getGrade12Gwa() {
        return grade12Gwa;
    }
    
    public void setGrade12Gwa(Double grade12Gwa) {
        this.grade12Gwa = grade12Gwa;
    }
    
    public String getHonorsReceived() {
        return honorsReceived;
    }
    
    public void setHonorsReceived(String honorsReceived) {
        this.honorsReceived = honorsReceived;
    }
    
    public String getFirstCollegeChoice() {
        return firstCollegeChoice;
    }
    
    public void setFirstCollegeChoice(String firstCollegeChoice) {
        this.firstCollegeChoice = firstCollegeChoice;
    }
    
    public String getSecondCollegeChoice() {
        return secondCollegeChoice;
    }
    
    public void setSecondCollegeChoice(String secondCollegeChoice) {
        this.secondCollegeChoice = secondCollegeChoice;
    }
    
    public String getThirdCollegeChoice() {
        return thirdCollegeChoice;
    }
    
    public void setThirdCollegeChoice(String thirdCollegeChoice) {
        this.thirdCollegeChoice = thirdCollegeChoice;
    }
    
    public String getFirstProgramChoice() {
        return firstProgramChoice;
    }
    
    public void setFirstProgramChoice(String firstProgramChoice) {
        this.firstProgramChoice = firstProgramChoice;
    }
    
    public String getSecondProgramChoice() {
        return secondProgramChoice;
    }
    
    public void setSecondProgramChoice(String secondProgramChoice) {
        this.secondProgramChoice = secondProgramChoice;
    }
    
    public String getThirdProgramChoice() {
        return thirdProgramChoice;
    }
    
    public void setThirdProgramChoice(String thirdProgramChoice) {
        this.thirdProgramChoice = thirdProgramChoice;
    }
    
    public String getEssay() {
        return essay;
    }
    
    public void setEssay(String essay) {
        this.essay = essay;
    }
    
    public Integer getEssayWordCount() {
        return essayWordCount;
    }
    
    public void setEssayWordCount(Integer essayWordCount) {
        this.essayWordCount = essayWordCount;
    }
    
    public String getApplicationStatus() {
        return applicationStatus;
    }
    
    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
    
    public String getReviewStatus() {
        return reviewStatus;
    }
    
    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }
    
    public LocalDateTime getInterviewDate() {
        return interviewDate;
    }
    
    public void setInterviewDate(LocalDateTime interviewDate) {
        this.interviewDate = interviewDate;
    }
    
    public String getInterviewStatus() {
        return interviewStatus;
    }
    
    public void setInterviewStatus(String interviewStatus) {
        this.interviewStatus = interviewStatus;
    }
    
    public String getFinalDecision() {
        return finalDecision;
    }
    
    public void setFinalDecision(String finalDecision) {
        this.finalDecision = finalDecision;
    }
    
    public String getDecisionNotes() {
        return decisionNotes;
    }
    
    public void setDecisionNotes(String decisionNotes) {
        this.decisionNotes = decisionNotes;
    }
    
    public String getDocumentsJson() {
        return documentsJson;
    }
    
    public void setDocumentsJson(String documentsJson) {
        this.documentsJson = documentsJson;
    }
    
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
    
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        // Generate reference number
        if (referenceNumber == null) {
            referenceNumber = "HF-" + System.currentTimeMillis() + "-" + 
                String.format("%04d", (int)(Math.random() * 10000));
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScholarshipApplication that = (ScholarshipApplication) o;
        return Objects.equals(id, that.id) && 
               Objects.equals(referenceNumber, that.referenceNumber);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, referenceNumber);
    }
    
    @Override
    public String toString() {
        return "ScholarshipApplication{" +
                "id=" + id +
                ", referenceNumber='" + referenceNumber + '\'' +
                ", applicationStatus='" + applicationStatus + '\'' +
                '}';
    }
}