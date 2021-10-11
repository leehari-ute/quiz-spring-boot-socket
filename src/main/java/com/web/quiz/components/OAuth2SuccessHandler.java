package com.web.quiz.components;

import com.web.quiz.models.User;
import com.web.quiz.services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        String redirectUrl = request.getContextPath();
        Object principal = authentication.getPrincipal();
        JSONObject jsonPrincipal = new JSONObject(principal);
        String email = jsonPrincipal.get("email").toString();
        Optional<User> user = this.userService.getUserByEmail(email);
        if (!user.isPresent()) {
            redirectUrl += "/auth/signup-google";
        } else {
            if (user.get().getRole().equalsIgnoreCase("student")) {
                redirectUrl += "/home";
            } else {
                redirectUrl += "/dashboard";
            }
        }
        response.sendRedirect(redirectUrl);
    }
}
