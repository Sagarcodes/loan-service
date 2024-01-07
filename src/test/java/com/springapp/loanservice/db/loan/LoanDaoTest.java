package com.springapp.loanservice.db.loan;

import com.springapp.loanservice.db.loan.entity.Loan;
import com.springapp.loanservice.enums.LoanStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static com.springapp.loanservice.util.Constants.SIMPLE_LOAN_INTEREST;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LoanDaoTest {

    @Autowired
    private LoanDao loanDao;
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

    @BeforeEach
    void setUp() {
        loanDao.save(LOAN);
    }

    @AfterEach
    void tearDown() {
        loanDao.deleteAll();
    }

    @Test
    void findByUsernameSuccess() {
        List<Loan> actual = loanDao.findByUsername(USERNAME);
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(USERNAME, actual.get(0).getUsername());
    }

    @Test
    void findByIdAndUsernameSuccess() {
        Loan actual = loanDao.findByIdAndUsername(2, USERNAME);
        assertNotNull(actual);
        assertEquals(USERNAME, actual.getUsername());
    }
}