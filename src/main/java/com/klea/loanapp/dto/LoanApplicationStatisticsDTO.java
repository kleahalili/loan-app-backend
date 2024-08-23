package com.klea.loanapp.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoanApplicationStatisticsDTO {
    private long applied;
    private long approved;
    private long rejected;
    private long documentsRequested;
//    private long documentsSubmitted;

    public LoanApplicationStatisticsDTO(long applied, long approved, long rejected, long documentsRequested) {
        this.applied = applied;
        this.approved = approved;
        this.rejected = rejected;
        this.documentsRequested = documentsRequested;
//        this.documentsSubmitted=documentsSubmitted;

    }
}
