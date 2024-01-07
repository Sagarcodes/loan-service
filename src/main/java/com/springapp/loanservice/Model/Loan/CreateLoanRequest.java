package com.springapp.loanservice.Model.Loan;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class CreateLoanRequest {
    private String username;
    private double amount;
    private int term;
}
