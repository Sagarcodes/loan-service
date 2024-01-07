package com.springapp.loanservice.exceptions;

public class LoanServiceException extends RuntimeException {
    public LoanServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
