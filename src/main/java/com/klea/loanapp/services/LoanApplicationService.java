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
        // Add any additional business logic/validation here before saving the loan application
        return loanApplicationRepository.save(loanApplication);
    }

    public void updateLoanApplicationStatus(Long applicationId, String status) {
        // Fetch loan application by ID
        LoanApplication loanApplication = loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Loan application not found with id: " + applicationId));

        // Update the status
        loanApplication.setApplicationStatus(status);

        // Save the updated loan application
        loanApplicationRepository.save(loanApplication);
    }

    // Add the findById method
    public Optional<LoanApplication> findById(Long applicationId) {
        return loanApplicationRepository.findById(applicationId);
    }

    // Add the save method to update the LoanApplication
    public LoanApplication save(LoanApplication loanApplication) {
        return loanApplicationRepository.save(loanApplication);
    }

    // New method to get loan application statistics for "Applied", "Approved", and "Rejected"
    public LoanApplicationStatisticsDTO getLoanApplicationStatistics() {
        long appliedCount = loanApplicationRepository.countByApplicationStatus("Applied");
        long approvedCount = loanApplicationRepository.countByApplicationStatus("Approved");
        long rejectedCount = loanApplicationRepository.countByApplicationStatus("Rejected");

        return new LoanApplicationStatisticsDTO(appliedCount, approvedCount, rejectedCount);
    }
}
