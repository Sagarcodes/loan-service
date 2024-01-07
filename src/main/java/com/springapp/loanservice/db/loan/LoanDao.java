package com.springapp.loanservice.db.loan;

import com.springapp.loanservice.db.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanDao extends JpaRepository<Loan, Integer> {

    List<Loan> findByUsername(String username);

    Loan findByIdAndUsername(int id, String username);
}
