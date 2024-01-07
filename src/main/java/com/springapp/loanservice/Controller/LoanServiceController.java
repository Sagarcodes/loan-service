package com.springapp.loanservice.Controller;

import com.springapp.loanservice.Db.Entity.Loan.LoanInstalment;
import com.springapp.loanservice.Model.Loan.CreateLoanRequest;
import com.springapp.loanservice.Model.Loan.LoanDto;
import com.springapp.loanservice.Model.Loan.PayInstalmentRequest;
import com.springapp.loanservice.Service.LoanService;
import com.springapp.loanservice.Util.LoanEntityToDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * TODO:
 * 1. Fix exception handling
 * 2. Fix request response
 * 3. RBAC - only admin can use approveLoan api
 *         - user can see only their own loan
 */
@RestController
@RequestMapping(path = "/api/v1/loan")
public class LoanServiceController {

    @Autowired
    private LoanService loanService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/{username}")
    public ResponseEntity<List<LoanDto>> getLoan(@PathVariable String username) {
        List<LoanDto> loans = LoanEntityToDtoMapper.map(loanService.getLoanByUserName(username));
        return ResponseEntity.of(Optional.of(loans));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(path = "/create")
    public ResponseEntity<Void> createLoan(@RequestBody CreateLoanRequest request) {
        loanService.createLoan(request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('admin:update')")
    @PutMapping(path = "/approve/{loanId}")
    public ResponseEntity<Void> approveLoan(@PathVariable int loanId) {
        loanService.approveLoan(loanId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(path = "/instalment")
    public ResponseEntity<LoanInstalment> payInstalment(@RequestBody PayInstalmentRequest request) {
        loanService.payInstalment(request);
        return ResponseEntity.ok().build();
    }

}
