package com.web.quiz.controllers;

import com.web.quiz.models.User;
import com.web.quiz.services.AuthService;
import com.web.quiz.services.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, AuthService authService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String getViewLogin(Authentication authentication) {
        if (this.authService.isAuth()) {
            Optional<User> user = this.userService.getUserByAuthentication(authentication);
            String role = this.userService.getRoleUser(user);
            return this.userService.redirectBaseRole(role);
        } else {
            return "/auth/login";
        }
    }

    @GetMapping("/signup")
    public ModelAndView getViewSignup(Model model, Authentication authentication) {
        if (this.authService.isAuth()) {
            Optional<User> user = this.userService.getUserByAuthentication(authentication);
            String role = this.userService.getRoleUser(user);
            return new ModelAndView(this.userService.redirectBaseRole(role));
        } else {
            model.addAttribute("step", 1);
            model.addAttribute("isOAuth2", false);
            model.addAttribute("users", this.userService.getAllUsername());
            return new ModelAndView("/auth/signup");
        }
    }

    @GetMapping("/signup-google")
    public ModelAndView getViewSignupGoogle(Model model, Authentication authentication) {
        if (authentication != null) {
            JSONObject principal = new JSONObject(authentication.getPrincipal());
            try {
                String email = principal.get("email").toString();
                String name = principal.get("fullName").toString();
                model.addAttribute("email", email);
                model.addAttribute("name", name);
                model.addAttribute("isOAuth2", true);
                model.addAttribute("step", 3);
                return new ModelAndView("/auth/signup");
            } catch (JSONException ignored) {
                return new ModelAndView("redirect:/home");
            }
        }
        return new ModelAndView("redirect:/home");
    }

    @PostMapping("/signup")
    public String handleSignup(@ModelAttribute User user) {
        user.setAvt("https://res.cloudinary.com/leehari/image/upload/v1631798864/avt_kvg6uh.png");
        if (user.getAuthType().equalsIgnoreCase("local")) {
            String password = user.getPassword(); //giu mat khau de auto login
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
            user.setUsername(user.getUsername().toLowerCase());
            this.userService.saveUser(user);
            this.authService.autoLogin(user.getUsername(), password);
            return "redirect:/home";
        } else {
            this.userService.saveUser(user);
            if (user.getRole().equalsIgnoreCase("student")) {
                return "redirect:/home";
            } else {
                return "redirect:/dashboard";
            }
        }
    }
}
