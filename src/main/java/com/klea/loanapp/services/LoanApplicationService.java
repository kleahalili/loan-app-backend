package com.klea.loanapp.services;

import com.klea.loanapp.dto.LoanApplicationStatisticsDTO;
import com.klea.loanapp.entities.LoanApplication;
import com.klea.loanapp.repositories.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanApplicationService {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private SendEmailService sendEmailService;

    public List<LoanApplication> findAllLoanApplications() {
        return loanApplicationRepository.findAll();
    }

    public LoanApplication createLoanApplication(LoanApplication loanApplication) {
        return loanApplicationRepository.save(loanApplication);
    }

    public String updateLoanApplicationStatus(Long applicationId, String status) {
        LoanApplication loanApplication = loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Loan application not found with id: " + applicationId));

        loanApplication.setApplicationStatus(status);
        loanApplicationRepository.save(loanApplication);

        // Prepare email subject and body based on the status
        String subject;
        String body;

        if ("Approved".equalsIgnoreCase(status)) {
            subject = "Your Loan Application is Approved";
            body = "Congratulations, your loan application has been approved.";
        } else if ("Rejected".equalsIgnoreCase(status)) {
            subject = "Your Loan Application is Rejected";
            body = "We regret to inform you that your loan application has been rejected.";
        } else {
            // Return a message if no email was sent
            return "Status updated without sending email.";
        }

        // Get the user's name from the loan application
        String userName = loanApplication.getFirstName();

        // Send the email with a properly formatted message
        sendEmailService.sendEmail(loanApplication.getEmailAddress(), subject, body, userName);

        // Return a message indicating that the status was updated and email was sent
        return "Status updated and email sent to " + loanApplication.getEmailAddress();
    }

    public Optional<LoanApplication> findById(Long applicationId) {
        return loanApplicationRepository.findById(applicationId);
    }

    public LoanApplication save(LoanApplication loanApplication) {
        return loanApplicationRepository.save(loanApplication);
    }

    public void requestDocumentUpload(Long applicationId) {
        LoanApplication loanApplication = loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Loan application not found with id: " + applicationId));

        loanApplication.setDocumentsRequested(true);
        loanApplication.setApplicationStatus("Documents Requested");

        loanApplicationRepository.save(loanApplication);
    }

    public LoanApplicationStatisticsDTO getLoanApplicationStatistics() {
        long appliedCount = loanApplicationRepository.countByApplicationStatus("Applied");
        long approvedCount = loanApplicationRepository.countByApplicationStatus("Approved");
        long rejectedCount = loanApplicationRepository.countByApplicationStatus("Rejected");
        long documentsRequestedCount = loanApplicationRepository.countByApplicationStatus("Documents Requested");

        return new LoanApplicationStatisticsDTO(appliedCount, approvedCount, rejectedCount, documentsRequestedCount);
    }

    // Method to delete a loan application by its ID
    public void deleteLoanApplication(Long applicationId) {
        if (loanApplicationRepository.existsById(applicationId)) {
            loanApplicationRepository.deleteById(applicationId);
        } else {
            throw new IllegalArgumentException("Loan application not found with id: " + applicationId);
        }
    }
}