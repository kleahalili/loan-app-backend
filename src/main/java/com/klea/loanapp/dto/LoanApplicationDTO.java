package com.klea.loanapp.dto;

import java.math.BigDecimal;
import java.util.Date;

public class LoanApplicationDTO {
    private Long applicationId;
    private String firstName;
    private String lastName;
    private BigDecimal loanAmount;
    private String loanDuration;
    private Date submittedAt;
    private String applicationStatus;
    private boolean documentUploaded;

    public LoanApplicationDTO(Long applicationId, String firstName, String lastName, BigDecimal loanAmount,
                              String loanDuration, Date submittedAt, String applicationStatus, boolean documentUploaded) {
        this.applicationId = applicationId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.loanAmount = loanAmount;
        this.loanDuration = loanDuration;
        this.submittedAt = submittedAt;
        this.applicationStatus = applicationStatus;
        this.documentUploaded = documentUploaded;
    }

    // Getters and setters
    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getLoanDuration() {
        return loanDuration;
    }

    public void setLoanDuration(String loanDuration) {
        this.loanDuration = loanDuration;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public boolean isDocumentUploaded() {
        return documentUploaded;
    }

    public void setDocumentUploaded(boolean documentUploaded) {
        this.documentUploaded = documentUploaded;
    }
}