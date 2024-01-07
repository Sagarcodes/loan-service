package com.springapp.loanservice.Service;

import com.springapp.loanservice.Db.Dao.Loan.LoanDao;
import com.springapp.loanservice.Db.Dao.Loan.LoanInstalmentDao;
import com.springapp.loanservice.Db.Entity.Loan.Loan;
import com.springapp.loanservice.Db.Entity.Loan.LoanInstalment;
import com.springapp.loanservice.Enums.InstalmentStatus;
import com.springapp.loanservice.Enums.LoanStatus;
import com.springapp.loanservice.Model.Loan.CreateLoanRequest;
import static com.springapp.loanservice.Util.Constants.SIMPLE_LOAN_INTEREST;

import com.springapp.loanservice.Model.Loan.PayInstalmentRequest;
import com.springapp.loanservice.Util.InstalmentCalculator;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {
    @Autowired
    private LoanDao loanDao;
    @Autowired
    private LoanInstalmentDao loanInstalmentDao;
    @Autowired
    @Qualifier("SimpleLoanInstalmentCalculator")
    private InstalmentCalculator instalmentCalculator;

    public void createLoan(@NonNull CreateLoanRequest request) {
        Loan loan = Loan.builder()
                .username(request.getUsername())
                .amount(request.getAmount())
                .remainingAmount(request.getAmount())
                .instalmentAmount(instalmentCalculator.calculate(request.getAmount(), request.getTerm()))
                .interest(SIMPLE_LOAN_INTEREST)
                .term(request.getTerm())
                .remainingTerm(request.getTerm())
                .status(LoanStatus.PENDING)
                .dateApplied(LocalDateTime.now())
                .build();
        loanDao.save(loan);
    }

    public Loan getLoanByLoanId(int loanId) {
        return loanDao.getReferenceById(loanId);
    }

    public List<Loan> getLoanByUserName(String username) {
        return loanDao.findByUsername(username);
    }

    public void approveLoan(int loanId) {
        Loan loan = loanDao.getReferenceById(loanId);
        // TODO: exception handling - if loan not found
        loan.setStatus(LoanStatus.APPROVED);
        loan.setDateIssued(LocalDateTime.now());
        loan.setNextDueDate(loan.getDateIssued().plusWeeks(1));
        loanDao.save(loan);

        // create first loan instalment in loanInstalment table
        LoanInstalment instalment = LoanInstalment.builder()
                .loanId(loan.getId())
                .amount(loan.getInstalmentAmount())
                .dueDate(loan.getNextDueDate())
                .status(InstalmentStatus.PENDING)
                .build();
        loanInstalmentDao.save(instalment);
    }

    public void payInstalment(PayInstalmentRequest request) {
        Loan loan = getLoanByLoanId(request.getLoanId());
        if (loan == null) {
            throw new RuntimeException("No loan exists for loan id: " + request.getLoanId());
        }
        if (loan.getRemainingTerm() == 0 || loan.getRemainingAmount() == 0) {
            throw new RuntimeException("Loan is already paid. Loan id: " + request.getLoanId());
        }
        LoanInstalment instalment = getPendingLoanInstalment(request.getLoanId());
        if (request.getAmount() < instalment.getAmount()) {
            throw new RuntimeException("Amount less than required instalment amount");
        }

        instalment.setStatus(InstalmentStatus.PAID);
        instalment.setPaidOn(LocalDateTime.now());
        loanInstalmentDao.save(instalment);

        loan.setNextDueDate(loan.getNextDueDate().plusWeeks(1));
        loan.setRemainingTerm(loan.getRemainingTerm()-1);
        loan.setRemainingAmount(loan.getRemainingAmount() - instalment.getAmount());
        loanDao.save(loan);

        //create next instalment
        if (loan.getRemainingTerm() > 0) {
            LoanInstalment nextLoanInstalment = LoanInstalment.builder()
                    .loanId(loan.getId())
                    .amount(loan.getInstalmentAmount())
                    .dueDate(loan.getNextDueDate())
                    .status(InstalmentStatus.PENDING)
                    .build();
        }
    }

    private LoanInstalment getPendingLoanInstalment(int loanId) {
        List<LoanInstalment> instalments =
                loanInstalmentDao.findByLoanIdAndStatus(loanId, InstalmentStatus.PENDING);
        // there can be only one pending instalment at a time
        if (instalments == null || instalments.isEmpty()) {
            throw new RuntimeException("no pending instalment exists for loanId: " + loanId);
        }

        return instalments.get(0);
    }

}
