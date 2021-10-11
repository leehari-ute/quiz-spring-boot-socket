package com.web.quiz.controllers;

import com.web.quiz.models.Game;
import com.web.quiz.models.Player;
import com.web.quiz.models.Quiz;
import com.web.quiz.models.User;
import com.web.quiz.services.*;
import com.web.quiz.utils.HelperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;
import java.util.Optional;

@Controller
public class PracticeController {
    private final GameService gameService;
    private final PlayerService playerService;
    private final QuizService quizService;
    private final AuthService authService;
    private final UserService userService;
    private final QuestionService questionService;

    @Autowired
    public PracticeController(GameService gameService,QuestionService questionService, UserService userService, AuthService authService, PlayerService playerService, QuizService quizService) {
        this.playerService = playerService;
        this.gameService = gameService;
        this.quizService = quizService;
        this.authService = authService;
        this.userService = userService;
        this.questionService = questionService;
    }

    @GetMapping("/detail-quiz/{idQuiz}")
    public String getViewDetail(Model model, @PathVariable Integer idQuiz) {
        Optional<Quiz> quizOptional = this.quizService.findById(idQuiz);
        quizOptional.ifPresent(quiz -> model.addAttribute("quiz", quiz));
        return "/practice/playdetail-edit";
    }

    @GetMapping("/join/practice/{idQuiz}")
    public String getViewPlayPractice(Model model, @PathVariable Integer idQuiz, Authentication authentication) {
        Optional<Quiz> quizOptional = this.quizService.findByIdAndStatusAndIsPublish(idQuiz, "public", true);
        if (quizOptional.isPresent()) {
            System.out.println("found");
            Quiz quiz = quizOptional.get();
            Game game = this.gameService.saveGame(new Game(quiz, "practice"));
            String idPlayer = HelperUtils.generateID(10);
            while (this.playerService.findById(idPlayer).isPresent()) {
                idPlayer = HelperUtils.generateID(10);
            }
            String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
            Player player = new Player(idPlayer, game, sessionId, "in", 0, 0);
            if (this.authService.isAuth()) {
                Optional<User> user = this.userService.getUserByAuthentication(authentication);
                user.ifPresent(player::setIdUser);
            }
            player = this.playerService.savePlayer(player);
            model.addAttribute("idPlayer", player.getId());
            model.addAttribute("point", player.getPoint());
            List<Integer> listQuestionId = this.questionService.getListQuestionIdByQuiz(quiz);
            model.addAttribute("questionIds", listQuestionId);
            model.addAttribute("idQuiz", idQuiz);
            return "/practice/in-game";
        }
        return "redirect:/error/404";
    }
}
