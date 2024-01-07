package com.springapp.loanservice.Util;

import com.springapp.loanservice.Db.Entity.Loan.Loan;
import com.springapp.loanservice.Model.Loan.LoanDto;

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
