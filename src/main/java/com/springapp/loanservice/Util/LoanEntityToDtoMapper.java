package com.springapp.loanservice.Util;

import com.springapp.loanservice.Db.Entity.Loan.Loan;
import com.springapp.loanservice.Model.Loan.LoanDto;

import java.util.ArrayList;
import java.util.List;

public class LoanEntityToDtoMapper {
    public static LoanDto map(Loan loan) {
        return LoanDto.builder()
                .id(loan.getId())
                .username(loan.getUsername())
                .amount(loan.getAmount())
                .remainingAmount(loan.getRemainingAmount())
                .instalmentAmount(loan.getInstalmentAmount())
                .interest(loan.getInterest())
                .term(loan.getTerm())
                .remainingTerm(loan.getRemainingTerm())
                .nextDueDate(loan.getNextDueDate())
                .status(loan.getStatus())
                .dateApplied(loan.getDateApplied())
                .dateIssued(loan.getDateIssued())
                .instalmentAmount(loan.getInstalmentAmount())
                .build();
    }

    public static List<LoanDto> map(List<Loan> loans) {
        List<LoanDto> loanDtos = new ArrayList<>();
        for (Loan loan : loans) {
            loanDtos.add(LoanEntityToDtoMapper.map(loan));
        }
        return loanDtos;
    }
}
