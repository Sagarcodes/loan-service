package com.springapp.loanservice.Db.Dao.Loan;

import com.springapp.loanservice.Db.Entity.Loan.LoanInstalment;
import com.springapp.loanservice.Enums.InstalmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanInstalmentDao extends JpaRepository<LoanInstalment, Integer> {

    List<LoanInstalment> findByLoanIdAndStatus(int loanId, InstalmentStatus status);
}
