package com.web.quiz.controllers;


import com.web.quiz.constant.Role;
import com.web.quiz.models.Quiz;
import com.web.quiz.models.Subject;
import com.web.quiz.models.User;
import com.web.quiz.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class QuizController {
    private final SubjectService subjectService;
    private final QuizService quizService;
    private final UserService userService;
    private final TimeQuestionService timeQuestionService;
    private final GradeService gradeService;
    private final QuestionService questionService;

    @Autowired
    public QuizController(SubjectService subjectService,QuestionService questionService, GradeService gradeService, QuizService quizService, UserService userService, TimeQuestionService timeQuestionService) {
        this.subjectService = subjectService;
        this.quizService = quizService;
        this.userService = userService;
        this.timeQuestionService = timeQuestionService;
        this.gradeService = gradeService;
        this.questionService = questionService;
    }

    @GetMapping({ "/editor-quiz/create"})
    public String getCreateQuizView(Model model) {
        List<Subject> subjectList = this.subjectService.findAllSubject();
        model.addAttribute("subjects", subjectList);
        model.addAttribute("quiz", new Quiz());
        return "quiz/create-quiz";
    }

    @PostMapping({"/editor-quiz", "/editor-quiz/"})
    public ModelAndView redirectCreateQuiz(@ModelAttribute("quiz") Quiz quiz, BindingResult result, Authentication authentication) {
        if (result.hasErrors()) {
//            System.out.println(result.getModel());
            return new ModelAndView("redirect:editor-quiz/create");
        } else {
            Optional<User> user = this.userService.getUserByAuthentication(authentication);
            if (user.isPresent()) {
                quiz.setIdOwner(user.get());
                quiz.setStatus("public");
                quiz.setIsPublish(false);
                quiz.setNumberQuestions(0);
                quiz.setFromGrade("1 st");
                quiz.setToGrade("1 st");
                quiz.setTitleImage("https://res.cloudinary.com/leehari/image/upload/v1631807522/img_unqydp.png");
                quiz = this.quizService.insertQuiz(quiz);
            }
            String redirectUrl = "redirect:editor-quiz/" + quiz.getId().toString();
//            System.out.println(redirectUrl);
            return new ModelAndView(redirectUrl);
        }
    }

    @GetMapping({ "/editor-quiz/{idQuiz}"})
    public String getEditorQuizView(
            @PathVariable Integer idQuiz,
            Authentication authentication,
            Model model) {
        Optional<User> user = this.userService.getUserByAuthentication(authentication);
        if (user.isPresent()) {
            Optional<Quiz> quizOptional = this.quizService.findByIdAndIdOwner(idQuiz, user.get());
            if (quizOptional.isPresent()) {
//                System.out.println(quizOptional.get().getQuestions());
                model.addAttribute("subjects", this.subjectService.findAllSubject());
                model.addAttribute("grades", this.gradeService.findByOrderByOrder());
                model.addAttribute("times", this.timeQuestionService.getAll());
                Quiz quiz = quizOptional.get();
                quiz.setIsPublish(false);
                long questionAmount = this.questionService.countQuestionInQuiz(quiz);
                quiz.setNumberQuestions((int) questionAmount);
                this.quizService.insertQuiz(quiz);
                model.addAttribute("quiz", quiz);
                return "quiz/edit-quiz";
            }
            return "redirect:/error/404";
        }
        return "redirect:/auth/login";
    }

    @PostMapping("/editor-quiz/delete-quiz/{idQuiz}")
    public String deleteQuiz(@PathVariable Integer idQuiz,
                                   Authentication authentication) {
        Optional<User> user = this.userService.getUserByAuthentication(authentication);
        if (user.isPresent()) {
            if (this.quizService.findById(idQuiz).isPresent()) {
                this.quizService.deleteById(idQuiz);
                if (user.get().getRole().equalsIgnoreCase(Role.TEACHER)) {
                    return "redirect:/dashboard";
                }
                return"redirect:/home";
            }
            return "redirect:/error";
        }
        return "redirect:/error";
    }

    @GetMapping({ "/detail/{idQuiz}"})
    public String getDetailQuiz(@PathVariable Integer idQuiz, Model model) {
        Optional<Quiz> quizOptional = this.quizService.findById(idQuiz);
        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            model.addAttribute("author", quiz.getIdOwner().getName());
            model.addAttribute("quiz", quiz);
            return "quiz/detail";
        }
        return "redirect:/error/404";
    }
}

