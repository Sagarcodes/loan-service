package com.springapp.loanservice.db.loan;

import com.springapp.loanservice.db.loan.entity.LoanInstalment;
import com.springapp.loanservice.enums.InstalmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanInstalmentDao extends JpaRepository<LoanInstalment, Integer> {

    List<LoanInstalment> findByLoanIdAndStatus(int loanId, InstalmentStatus status);
}
