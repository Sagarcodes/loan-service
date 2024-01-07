package com.springapp.loanservice.model.Loan;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateLoanRequest {
    @Positive(message = "Loan amount must be a positive number.")
    private double amount;
    @Positive(message = "Term must be a positive number.")
    private int term;
}
