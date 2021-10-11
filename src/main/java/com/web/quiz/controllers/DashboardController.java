package com.web.quiz.controllers;

import com.web.quiz.constant.Role;
import com.web.quiz.models.Quiz;
import com.web.quiz.models.User;
import com.web.quiz.services.AuthService;
import com.web.quiz.services.QuizService;
import com.web.quiz.services.UserService;
import com.web.quiz.utils.OffsetBasedPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private final UserService userService;
    private final AuthService authService;
    private final QuizService quizService;

    @Autowired
    public DashboardController(UserService userService, AuthService authService, QuizService quizService) {
        this.userService = userService;
        this.authService = authService;
        this.quizService = quizService;
    }

    @GetMapping({"/library",""})
    public String getViewDashboard(Model model, Authentication authentication) {
        if (this.authService.isAuth()) {
            Optional<User> user = this.userService.getUserByAuthentication(authentication);
            if (user.isPresent()) {
                Pageable pageable = new OffsetBasedPageRequest(8, 0);
                List<Quiz> quizList = this.quizService.findByUser(user.get(), pageable);
                model.addAttribute("quizzes", quizList);
                model.addAttribute("user", user.get());
            }
        }
        return "dashboard/library";
    }

    @GetMapping("/collection")
    public String getViewCollection(Model model, Authentication authentication) {
        if (this.authService.isAuth()) {
            Optional<User> userOptional = this.userService.getUserByAuthentication(authentication);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getRole().equalsIgnoreCase(Role.TEACHER)) {
                    model.addAttribute("user", user);
                    return "dashboard/collection";
                }
            }
        }
        return "redirect:/home";
    }

    @GetMapping("/reports")
    public String getViewReport(Model model, Authentication authentication) {
        if (this.authService.isAuth()) {
            Optional<User> userOptional = this.userService.getUserByAuthentication(authentication);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getRole().equalsIgnoreCase(Role.TEACHER)) {
                    model.addAttribute("user", user);
                    return "dashboard/report";
                }
            }
        }
        return "redirect:/home";
    }
}
