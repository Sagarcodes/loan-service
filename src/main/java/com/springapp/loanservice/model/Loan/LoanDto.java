package com.springapp.loanservice.model.Loan;

import com.springapp.loanservice.enums.LoanStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LoanDto {
    private int id;
    private String username;
    private double amount;
    private double remainingAmount;
    private double instalmentAmount;
    private double interest;
    private int term;
    private int remainingTerm;
    private LocalDateTime nextDueDate;
    private LoanStatus status;
    private LocalDateTime dateApplied;
    private LocalDateTime dateIssued;
}
