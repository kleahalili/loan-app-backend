package com.klea.loanapp.repositories;

import com.klea.loanapp.entities.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    long countByApplicationStatus(String status);
}
