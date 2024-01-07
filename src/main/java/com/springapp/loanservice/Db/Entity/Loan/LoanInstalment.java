package com.springapp.loanservice.Db.Entity.Loan;

import com.springapp.loanservice.Enums.InstalmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
@Entity(name = "LoanInstalment")
public class LoanInstalment {
    @Id
    @GeneratedValue
    @Column(
            updatable = false
    )
    private int id;
    private int loanId;
    private double amount;
    @NonNull
    private LocalDateTime dueDate;
    private LocalDateTime paidOn;
    @NonNull
    @Enumerated(EnumType.STRING)
    private InstalmentStatus status;
}
