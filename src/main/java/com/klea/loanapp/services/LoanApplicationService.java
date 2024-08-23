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

    public List<LoanApplication> findAllLoanApplications() {
        return loanApplicationRepository.findAll();
    }

    public LoanApplication createLoanApplication(LoanApplication loanApplication) {
        return loanApplicationRepository.save(loanApplication);
    }

    public void updateLoanApplicationStatus(Long applicationId, String status) {
        LoanApplication loanApplication = loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Loan application not found with id: " + applicationId));

        loanApplication.setApplicationStatus(status);
        loanApplicationRepository.save(loanApplication);
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
//        long documentsSubmittedCount = loanApplicationRepository.countByApplicationStatus("Documents Submitted");

        return new LoanApplicationStatisticsDTO(appliedCount, approvedCount, rejectedCount, documentsRequestedCount);
    }

    // New method to delete a loan application by its ID
    public void deleteLoanApplication(Long applicationId) {
        if (loanApplicationRepository.existsById(applicationId)) {
            loanApplicationRepository.deleteById(applicationId);
        } else {
            throw new IllegalArgumentException("Loan application not found with id: " + applicationId);
        }
    }
}
