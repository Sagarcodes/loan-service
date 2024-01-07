package com.springapp.loanservice.util;

import com.springapp.loanservice.db.loan.entity.Loan;
import com.springapp.loanservice.model.Loan.LoanDto;

public class LoanDtoToEntityMapper {
    public static Loan map(LoanDto loan) {
        return Loan.builder()
                .id(loan.getId())
                .amount(loan.getAmount())
                .term(loan.getTerm())
                .status(loan.getStatus())
                .dateApplied(loan.getDateApplied())
                .dateIssued(loan.getDateIssued())
                .instalmentAmount(loan.getInstalmentAmount())
                .build();
    }
}
