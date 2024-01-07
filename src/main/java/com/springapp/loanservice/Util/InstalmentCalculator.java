package com.springapp.loanservice.Util;

import lombok.NonNull;

public interface InstalmentCalculator {
    public abstract double calculate(@NonNull double principle, int term);
}
