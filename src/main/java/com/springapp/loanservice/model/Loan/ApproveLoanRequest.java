package com.springapp.loanservice.model.Loan;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApproveLoanRequest {
    @Positive(message = "loanId number must be present.")
    int loanId;
    @NotBlank(message = "username can't be null or blank.")
    String username;
}
