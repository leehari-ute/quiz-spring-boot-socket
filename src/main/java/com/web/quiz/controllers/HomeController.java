package com.web.quiz.controllers;

import com.web.quiz.models.Quiz;
import com.web.quiz.models.Subject;
import com.web.quiz.models.User;
import com.web.quiz.services.AuthService;
import com.web.quiz.services.QuizService;
import com.web.quiz.services.SubjectService;
import com.web.quiz.services.UserService;
import com.web.quiz.utils.OffsetBasedPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final UserService userService;
    private final AuthService authService;
    private final SubjectService subjectService;
    private final QuizService quizService;

    @Autowired
    public HomeController(UserService userService, AuthService authService, SubjectService subjectService, QuizService quizService) {
        this.userService = userService;
        this.authService = authService;
        this.subjectService = subjectService;
        this.quizService = quizService;
    }

    @GetMapping({"/", ""})
    public String getViewHome(Model model, Authentication authentication) {
        List<Subject> subjectList = this.subjectService.findAllSubject();
        model.addAttribute("subjects", subjectList);
        if (this.authService.isAuth()) {
            Optional<User> user = this.userService.getUserByAuthentication(authentication);
            if (user.isPresent()) {
                model.addAttribute("user", user.get());
                List<Quiz> quizList = this.quizService.findAllByIdOwner(user.get());
                model.addAttribute("quizzs", quizList);
            }
        }
        HashMap<String, List<Quiz>> listHashMap = new HashMap<String, List<Quiz>>();
        Pageable pageable = new OffsetBasedPageRequest(4, 0);
        for (Subject subject: subjectList) {
            listHashMap.put(subject.getName(), this.quizService.findAllBySubjectAndStatusAndIsPublish(subject, "public", true, pageable));
        }
        model.addAttribute("quizzes", listHashMap);
        return "home/home";
    }

    @RequestMapping({"/search"})
    public String searchQuiz(Authentication authentication, Model model, @RequestParam("name") String name, @RequestParam("subject") String subject){
        if (this.authService.isAuth()) {
            Optional<User> user = this.userService.getUserByAuthentication(authentication);
            user.ifPresent(value -> model.addAttribute("user", value));
        }
        List<Subject> subjectList = this.subjectService.findAllSubject();
        model.addAttribute("subjects", subjectList);
        if (subject == null || subject.trim().equalsIgnoreCase("")) {
            subject = "all";
        }
        List<Quiz> quizList = this.quizService.searchBySubject(new Subject(subject), name);
        model.addAttribute("quizList", quizList);
        model.addAttribute("name", name);
        return "home/search";
    }

}
