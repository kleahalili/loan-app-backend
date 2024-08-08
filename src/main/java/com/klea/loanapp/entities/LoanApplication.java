package com.klea.loanapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loan_applications")
public class LoanApplication {

    @Id
    @GeneratedValue
    private Long applicationId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String fatherName;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    private String placeOfBirth;

    @Column(nullable = false)
    private String emailAddress;

    @Column(nullable = false)
    private String phoneNumber;

    private String education;

    private String maritalStatus;

    @Column(nullable = false)
    private BigDecimal requestedAmount;

    @Column(nullable = false, length = 10)
    private String loanDuration;

    @Column(nullable = false, length = 50)
    private String loanType;

    @Column(nullable = false, length = 10)
    private String currency;

    @Column(nullable = false, length = 20)
    private String applicationStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // New fields for document upload
    @Column(name = "document_path")
    private String documentPath;

    @Column(name = "document_uploaded")
    private boolean documentUploaded = false;

    // Getters and setters for new fields (if not using Lombok)
}
