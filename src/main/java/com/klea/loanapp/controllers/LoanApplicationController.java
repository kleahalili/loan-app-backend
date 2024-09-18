package com.klea.loanapp.controllers;

import com.klea.loanapp.dto.LoanApplicationDTO;
import com.klea.loanapp.dto.LoanApplicationStatisticsDTO;
import com.klea.loanapp.entities.LoanApplication;
import com.klea.loanapp.entities.Role;
import com.klea.loanapp.entities.User;
import com.klea.loanapp.services.LoanApplicationService;
import com.klea.loanapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/loan-applications")
public class LoanApplicationController {

    @Autowired
    private LoanApplicationService loanApplicationService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<LoanApplicationDTO>> getAllLoanApplications() {
        List<LoanApplication> loanApplications = loanApplicationService.findAllLoanApplications();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        List<LoanApplicationDTO> dto;

        if (user.getRole().equals(Role.USER)) {
            dto = loanApplications.stream()
                    .filter(loan -> Objects.equals(user.getId(), loan.getUser().getId()))
                    .map(loan -> new LoanApplicationDTO(
                            loan.getApplicationId(),
                            loan.getFirstName(),
                            loan.getLastName(),
                            loan.getRequestedAmount(),
                            loan.getLoanDuration(),
                            loan.getSubmittedAt(),
                            loan.getApplicationStatus(),
                            loan.isDocumentUploaded()
                    ))
                    .collect(Collectors.toList());
        } else {
            dto = loanApplications.stream()
                    .map(loan -> new LoanApplicationDTO(
                            loan.getApplicationId(),
                            loan.getFirstName(),
                            loan.getLastName(),
                            loan.getRequestedAmount(),
                            loan.getLoanDuration(),
                            loan.getSubmittedAt(),
                            loan.getApplicationStatus(),
                            loan.isDocumentUploaded()
                    ))
                    .collect(Collectors.toList());
        }

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LoanApplication> createLoanApplication(@RequestBody LoanApplication loanApplication) {
        loanApplication.setSubmittedAt(new Date());
        loanApplication.setApplicationStatus("Applied");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        loanApplication.setUser(user);

        LoanApplication createdLoanApplication = loanApplicationService.createLoanApplication(loanApplication);
        return new ResponseEntity<>(createdLoanApplication, HttpStatus.CREATED);
    }

    @PutMapping("/{applicationId}")
    public ResponseEntity<String> updateLoanApplicationStatus(@PathVariable Long applicationId, @RequestParam String status) {
        loanApplicationService.updateLoanApplicationStatus(applicationId, status);
        return ResponseEntity.ok("Loan application status updated successfully.");
    }

    @PostMapping("/{applicationId}/request-document")
    public ResponseEntity<String> requestDocumentUpload(@PathVariable Long applicationId) {
        Optional<LoanApplication> loanApplicationOptional = loanApplicationService.findById(applicationId);

        if (loanApplicationOptional.isPresent()) {
            LoanApplication loanApplication = loanApplicationOptional.get();
            loanApplication.setApplicationStatus("Documents Requested");
            loanApplication.setDocumentUploaded(false);
            loanApplicationService.save(loanApplication);
            return ResponseEntity.ok("Document request has been sent.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan application not found.");
        }
    }

    @PostMapping("/{applicationId}/upload-document")
    public ResponseEntity<String> uploadDocument(@PathVariable Long applicationId,
                                                 @RequestParam("file") MultipartFile file) throws IOException {
        Optional<LoanApplication> loanApplicationOptional = loanApplicationService.findById(applicationId);

        if (loanApplicationOptional.isPresent()) {
            LoanApplication loanApplication = loanApplicationOptional.get();

            String fileName = file.getOriginalFilename();
            if (fileName != null && (fileName.endsWith(".pdf") || fileName.endsWith(".docx"))) {
                loanApplication.setDocument(file.getBytes());
                loanApplication.setDocumentFileName(fileName);
                loanApplication.setDocumentUploaded(true);
                loanApplicationService.save(loanApplication);
                return ResponseEntity.ok("Document uploaded successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only .pdf and .docx files are allowed.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan application not found.");
        }
    }

    @GetMapping("/{applicationId}/download-document")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long applicationId) {
        Optional<LoanApplication> loanApplicationOptional = loanApplicationService.findById(applicationId);

        if (loanApplicationOptional.isPresent()) {
            LoanApplication loanApplication = loanApplicationOptional.get();

            byte[] document = loanApplication.getDocument();
            if (document != null) {
                String fileName = "document_" + applicationId; // Set a default file name

                // Set the correct Content-Type based on the file extension
                String contentType = loanApplication.getDocumentFileName().endsWith(".pdf") ? "application/pdf" : "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(document);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{applicationId}/loan-details")
    public ResponseEntity<Map<String, Object>> getLoanDetailsByApplicationId(@PathVariable Long applicationId) {
        Optional<LoanApplication> loanApplicationOptional = loanApplicationService.findById(applicationId);

        if (loanApplicationOptional.isPresent()) {
            LoanApplication loanApplication = loanApplicationOptional.get();

            Map<String, Object> loanDetails = new HashMap<>();
            loanDetails.put("applicationId", loanApplication.getApplicationId());
            loanDetails.put("firstName", loanApplication.getFirstName());
            loanDetails.put("lastName", loanApplication.getLastName());
            loanDetails.put("fatherName", loanApplication.getFatherName());
            loanDetails.put("dateOfBirth", loanApplication.getDateOfBirth());
            loanDetails.put("placeOfBirth", loanApplication.getPlaceOfBirth());
            loanDetails.put("emailAddress", loanApplication.getEmailAddress());
            loanDetails.put("phoneNumber", loanApplication.getPhoneNumber());
            loanDetails.put("education", loanApplication.getEducation());
            loanDetails.put("maritalStatus", loanApplication.getMaritalStatus());
            loanDetails.put("requestedAmount", loanApplication.getRequestedAmount());
            loanDetails.put("loanDuration", loanApplication.getLoanDuration());
            loanDetails.put("loanType", loanApplication.getLoanType());
            loanDetails.put("currency", loanApplication.getCurrency());
            loanDetails.put("applicationStatus", loanApplication.getApplicationStatus());
            loanDetails.put("submittedAt", loanApplication.getSubmittedAt());

            return new ResponseEntity<>(loanDetails, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<String> deleteLoanApplication(@PathVariable Long applicationId) {
        Optional<LoanApplication> loanApplicationOptional = loanApplicationService.findById(applicationId);

        if (loanApplicationOptional.isPresent()) {
            loanApplicationService.deleteLoanApplication(applicationId);
            return ResponseEntity.ok("Loan application deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan application not found.");
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<LoanApplicationStatisticsDTO> getLoanApplicationStatistics() {
        LoanApplicationStatisticsDTO statisticsDTO = loanApplicationService.getLoanApplicationStatistics();
        return new ResponseEntity<>(statisticsDTO, HttpStatus.OK);
    }
}