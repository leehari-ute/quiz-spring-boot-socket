package com.web.quiz.components;

import com.web.quiz.models.UserDetailsImp;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        String redirectUrl = request.getContextPath();
        UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();
        String role = userDetails.getRole();
        if (role.equalsIgnoreCase("student")) {
            redirectUrl += "/home";
        } else {
            redirectUrl += "/dashboard";
        }
        response.sendRedirect(redirectUrl);
    }
}
