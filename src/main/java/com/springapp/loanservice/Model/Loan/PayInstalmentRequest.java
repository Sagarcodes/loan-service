package com.springapp.loanservice.Model.Loan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayInstalmentRequest {
    private int loanId;
    private double amount;
}
