package com.springapp.loanservice.model.Loan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateLoanRequest {
    private String username;
    private double amount;
    private int term;
}
