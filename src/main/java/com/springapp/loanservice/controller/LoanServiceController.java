package com.springapp.loanservice.controller;

import com.springapp.loanservice.db.loan.entity.LoanInstalment;
import com.springapp.loanservice.exceptions.BadRequestException;
import com.springapp.loanservice.exceptions.LoanServiceException;
import com.springapp.loanservice.model.Loan.ApproveLoanRequest;
import com.springapp.loanservice.model.Loan.CreateLoanRequest;
import com.springapp.loanservice.model.Loan.LoanDto;
import com.springapp.loanservice.model.Loan.PayInstalmentRequest;
import com.springapp.loanservice.service.LoanService;
import com.springapp.loanservice.util.LoanEntityToDtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.springapp.loanservice.util.UserUtil.getUsername;

@RestController
@RequestMapping(path = "/api/v1/loan")
public class LoanServiceController {

    @Autowired
    private LoanService loanService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/fetchAll")
    public ResponseEntity<List<LoanDto>> getLoan() throws BadRequestException {
        List<LoanDto> loans = LoanEntityToDtoMapper.map(loanService.getLoanByUsername(getUsername()));
        return ResponseEntity.of(Optional.of(loans));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(path = "/create")
    public ResponseEntity<Void> createLoan(@RequestBody @Valid CreateLoanRequest request)
            throws LoanServiceException {
        loanService.createLoan(request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('admin:update')")
    @PutMapping(path = "/approve")
    public ResponseEntity<Void> approveLoan(@RequestBody @Valid ApproveLoanRequest request) throws LoanServiceException, BadRequestException {
        loanService.approveLoan(request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(path = "/instalment")
    public ResponseEntity<LoanInstalment> payInstalment(@RequestBody @Valid PayInstalmentRequest request)
            throws LoanServiceException, BadRequestException {
        loanService.payInstalment(request);
        return ResponseEntity.ok().build();
    }

}
