package com.springapp.loanservice.db.loan.entity;

import com.springapp.loanservice.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
@Entity(name = "Loan")
public class Loan {
    @Id
    @GeneratedValue
    private int id;
    private String username;
    private double amount;
    private double remainingAmount;
    private double instalmentAmount;
    private double interest;
    private int term;
    private int remainingTerm;
    private LocalDateTime nextDueDate;
    @NonNull
    @Enumerated(EnumType.STRING)
    private LoanStatus status;
    private LocalDateTime dateApplied;
    private LocalDateTime dateIssued;
}
