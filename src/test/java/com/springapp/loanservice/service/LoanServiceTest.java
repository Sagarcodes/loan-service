package com.springapp.loanservice.service;

import com.springapp.loanservice.db.loan.LoanDao;
import com.springapp.loanservice.db.loan.LoanInstalmentDao;
import com.springapp.loanservice.db.loan.entity.Loan;
import com.springapp.loanservice.enums.LoanStatus;
import com.springapp.loanservice.exceptions.BadRequestException;
import com.springapp.loanservice.model.Loan.ApproveLoanRequest;
import com.springapp.loanservice.model.Loan.CreateLoanRequest;
import com.springapp.loanservice.util.InstalmentCalculator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;

import static com.springapp.loanservice.util.Constants.SIMPLE_LOAN_INTEREST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {
    @Mock
    private LoanDao loanDao;
    @Mock
    private LoanInstalmentDao loanInstalmentDao;
    @Mock
    private InstalmentCalculator instalmentCalculator;
    @InjectMocks
    private LoanService loanService;
    private final String USERNAME = "sagarsingh";
    private final Loan LOAN = Loan.builder()
            .username(USERNAME)
            .amount(1500.0)
            .remainingAmount(1500.0)
            .instalmentAmount(500.0)
            .interest(SIMPLE_LOAN_INTEREST)
            .term(3)
            .remainingTerm(3)
            .status(LoanStatus.PENDING)
            .dateApplied(LocalDateTime.now())
            .build();

    @BeforeAll
    static void setup() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("sagarsingh");
    }

    @Test
    void createLoan() {
        CreateLoanRequest request = CreateLoanRequest.builder()
                .amount(1500)
                .term(3)
                .build();
        Mockito.when(instalmentCalculator.calculate(Mockito.anyDouble(), Mockito.anyInt())).thenReturn(500.0);
        loanService.createLoan(request);
    }

    @Test
    void getLoanByUsername() throws BadRequestException {
        Mockito.when(loanDao.findByUsername(USERNAME)).thenReturn(List.of(LOAN));
        List<Loan> loan = loanService.getLoanByUsername(USERNAME);
        assertNotNull(loan);
        assertEquals(1, loan.size());
        assertEquals(USERNAME, loan.get(0).getUsername());
    }

    @Test
    void approveLoan() throws BadRequestException {
        ApproveLoanRequest approveLoanRequest = ApproveLoanRequest.builder()
                        .loanId(1)
                        .username(USERNAME)
                        .build();

        loanService.approveLoan(approveLoanRequest);
    }
}