package com.springapp.loanservice.service;

import com.springapp.loanservice.db.token.TokenDao;
import com.springapp.loanservice.db.token.entity.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenDao tokenDao;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        Optional<Token> storedToken = tokenDao.findByToken(jwt);
        if (storedToken.isPresent()) {
            Token token = storedToken.get();
            token.setExpired(true);
            token.setRevoked(true);
            tokenDao.save(token);
            SecurityContextHolder.clearContext();
        }
    }
}