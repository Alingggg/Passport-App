package com.example.model;

import java.time.LocalDateTime;

public class PassportApplication {
    private Integer userId;
    private String status;
    private String feedback;
    private String referenceId;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
    
    // Constructors
    public PassportApplication() {}
    
    public PassportApplication(Integer userId) {
        this.userId = userId;
        this.status = "Pending";
    }
    
    // Getters and Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    
    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }
    
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
}
