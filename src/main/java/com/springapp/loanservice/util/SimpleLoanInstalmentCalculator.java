package com.springapp.loanservice.util;

import org.springframework.stereotype.Component;

@Component("SimpleLoanInstalmentCalculator")
public class SimpleLoanInstalmentCalculator implements InstalmentCalculator {
    @Override
    public double calculate(double principle, int term) {
        return principle/term;
    }
}
