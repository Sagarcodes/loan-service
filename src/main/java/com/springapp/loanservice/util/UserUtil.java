package com.springapp.loanservice.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {
    public static String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
