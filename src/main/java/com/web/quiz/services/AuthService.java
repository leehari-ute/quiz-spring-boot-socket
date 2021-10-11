package com.web.quiz.services;

import com.web.quiz.interfaces.IAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuth {
    private final AuthenticationManager authenticationManager;
    @Lazy
    @Autowired
    public AuthService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean isAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        if (authentication.getPrincipal().toString().equals("anonymousUser")){
            return false;
        }
        return authentication.isAuthenticated();
    }

    @Override
    public void autoLogin(String username, String password) {
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);

        Authentication authentication = authenticationManager.authenticate(authRequest);

        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}
