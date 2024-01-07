package com.springapp.loanservice.Util;

import org.springframework.stereotype.Component;

@Component("SimpleLoanInstalmentCalculator")
public class SimpleLoanInstalmentCalculator implements InstalmentCalculator {
    @Override
    public double calculate(double principle, int term) {
        return principle/term;
    }
}
