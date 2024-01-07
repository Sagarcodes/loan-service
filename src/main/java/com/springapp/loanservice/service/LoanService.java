package com.springapp.loanservice.service;

import com.springapp.loanservice.db.loan.LoanDao;
import com.springapp.loanservice.db.loan.LoanInstalmentDao;
import com.springapp.loanservice.db.loan.entity.Loan;
import com.springapp.loanservice.db.loan.entity.LoanInstalment;
import com.springapp.loanservice.enums.InstalmentStatus;
import com.springapp.loanservice.enums.LoanStatus;
import com.springapp.loanservice.exceptions.BadRequestException;
import com.springapp.loanservice.exceptions.LoanServiceException;
import com.springapp.loanservice.model.Loan.ApproveLoanRequest;
import com.springapp.loanservice.model.Loan.CreateLoanRequest;
import static com.springapp.loanservice.util.Constants.SIMPLE_LOAN_INTEREST;
import static com.springapp.loanservice.util.UserUtil.getUsername;

import com.springapp.loanservice.model.Loan.PayInstalmentRequest;
import com.springapp.loanservice.util.InstalmentCalculator;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
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

    public void createLoan(@NonNull CreateLoanRequest request) throws LoanServiceException {
        try {
            Loan loan = Loan.builder()
                    .username(getUsername())
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
        } catch (DataAccessException ex) {
            String msg = String.format("Exception while saving loan with username: %s and amount: %f",
                    getUsername(), request.getAmount());
            throw new LoanServiceException(msg, ex);
        }
    }

    public List<Loan> getLoanByUsername(String username) throws BadRequestException {
        List<Loan> loans = loanDao.findByUsername(username);
        if (loans == null || loans.isEmpty()) {
            throw new BadRequestException("No loan found for username: " + username);
        }
        return loans;
    }

    public void approveLoan(ApproveLoanRequest request) throws LoanServiceException, BadRequestException {
        try {
            Loan loan = loanDao.findByIdAndUsername(request.getLoanId(), request.getUsername());
            if (loan == null) {
                String msg = String.format("No loan found for loanId: %d and username: %s.",
                        request.getLoanId(), request.getUsername());
                throw new BadRequestException(msg);
            }
            if (LoanStatus.APPROVED.equals(loan.getStatus())) {
                String msg = String.format("Loan with loanId: %d and username: %s is already approved.",
                        request.getLoanId(), request.getUsername());
                throw new BadRequestException(msg);
            }
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
        } catch (DataAccessException ex) {
            String msg = String.format("Exception while accessing loan db for loanId: %d and username: %s",
                    request.getLoanId(), request.getUsername());
            throw new LoanServiceException(msg, ex);
        }
    }

    public void payInstalment(PayInstalmentRequest request) throws LoanServiceException, BadRequestException {
        try {
            Loan loan = loanDao.findByIdAndUsername(request.getLoanId(), getUsername());
            if (loan == null) {
                String msg = String.format("No loan found for loanId: %d and username: %s",
                        request.getLoanId(), getUsername());
                throw new BadRequestException(msg);
            }
            if (loan.getRemainingAmount() == 0) {
                String msg = String.format("Loan is already fully paid for loanId: %d and username: %s",
                        request.getLoanId(), getUsername());
                throw new BadRequestException(msg);
            }
            LoanInstalment instalment = getPendingLoanInstalment(request.getLoanId());
            if (request.getAmount() < instalment.getAmount()) {
                String msg = String.format("Amount less than required instalment amount for loan Id: %d and username: %s",
                        request.getLoanId(), getUsername());
                throw new BadRequestException(msg);
            }

            instalment.setStatus(InstalmentStatus.PAID);
            instalment.setPaidOn(LocalDateTime.now());
            loanInstalmentDao.save(instalment);

            loan.setNextDueDate(loan.getNextDueDate().plusWeeks(1));
            loan.setRemainingTerm(loan.getRemainingTerm()-1);
            loan.setRemainingAmount(loan.getRemainingAmount() - instalment.getAmount());
            if (loan.getRemainingAmount() == 0) {
                loan.setStatus(LoanStatus.PAID);
            }
            loanDao.save(loan);

            //create next instalment
            if (loan.getRemainingTerm() > 0) {
                LoanInstalment nextLoanInstalment = LoanInstalment.builder()
                        .loanId(loan.getId())
                        .amount(loan.getInstalmentAmount())
                        .dueDate(loan.getNextDueDate())
                        .status(InstalmentStatus.PENDING)
                        .build();
                loanInstalmentDao.save(nextLoanInstalment);
            }
        } catch (DataAccessException ex) {
            String msg = String.format("Exception while accessing loan db for loanId: %d and username: %s",
                    request.getLoanId(), getUsername());
            throw new LoanServiceException(msg, ex);
        }
    }

    private LoanInstalment getPendingLoanInstalment(int loanId) throws BadRequestException {
        List<LoanInstalment> instalments =
                loanInstalmentDao.findByLoanIdAndStatus(loanId, InstalmentStatus.PENDING);
        // there can be only one pending instalment at a time
        if (instalments == null || instalments.isEmpty()) {
            throw new BadRequestException("No pending instalment exists for loanId: " + loanId);
        }

        return instalments.get(0);
    }

}
