package com.klea.loanapp.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoanApplicationStatisticsDTO {
    private long applied;
    private long approved;
    private long rejected;

    public LoanApplicationStatisticsDTO(long applied, long approved, long rejected) {
        this.applied = applied;
        this.approved = approved;
        this.rejected = rejected;
    }
}
