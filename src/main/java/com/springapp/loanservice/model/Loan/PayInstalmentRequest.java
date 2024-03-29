package com.springapp.loanservice.model.Loan;

import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayInstalmentRequest {
    @Positive(message = "loanId number must be present.")
    private int loanId;
    @Positive(message = "amount must be positive number.")
    private double amount;
}
