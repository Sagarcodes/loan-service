package com.springapp.loanservice.Db.Dao.Loan;

import com.springapp.loanservice.Db.Entity.Loan.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanDao extends JpaRepository<Loan, Integer> {

    List<Loan> findByUsername(String username);
}
