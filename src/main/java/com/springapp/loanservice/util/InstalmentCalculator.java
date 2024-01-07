package com.springapp.loanservice.util;

import lombok.NonNull;

public interface InstalmentCalculator {
    public abstract double calculate(double principle, int term);
}
