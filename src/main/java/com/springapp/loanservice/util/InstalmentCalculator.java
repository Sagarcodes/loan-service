package com.springapp.loanservice.util;

import lombok.NonNull;

public interface InstalmentCalculator {
    public abstract double calculate(@NonNull double principle, int term);
}
