package com.web.quiz.controllers;

import com.web.quiz.models.*;
import com.web.quiz.services.*;
import com.web.quiz.utils.HelperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@RestController
@RequestMapping("/practice")
public class PracticeRestController {
    private final GameService gameService;
    private final QuizService quizService;
    private final PlayerService playerService;
    private final QuestionService questionService;
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public PracticeRestController(GameService gameService, AuthService authService, QuizService quizService, PlayerService playerService, QuestionService questionService, UserService userService) {
        this.gameService = gameService;
        this.quizService = quizService;
        this.playerService = playerService;
        this.questionService = questionService;
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/get-question/{idQuiz}/{idQues}")
    public ModelAndView getQuestion(@PathVariable Integer idQues, @PathVariable Integer idQuiz) {
        ModelAndView modelAndView = new ModelAndView("practice/question");
        Optional<Question> question = this.questionService.findByPrimaryKey(new Question.PrimaryKey(idQues, idQuiz));
        question.ifPresent(value -> modelAndView.addObject("question", value));
        return modelAndView;
    }

    @PostMapping("/save-point/{idPlayer}/{point}")
    public Response savePoint(@PathVariable String idPlayer, @PathVariable Integer point) {
        Optional<Player> playerOptional = this.playerService.findById(idPlayer);
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            player.setPoint(point);
            this.playerService.savePlayer(player);
            return new Response("Successfully", 200, true, "");
        }
        return new Response("Error", 500, false, "");
    }

    @GetMapping("/get-result/{idPlayer}/{correctPercent}")
    public ModelAndView getResult(@PathVariable String idPlayer, @PathVariable Integer correctPercent) {
        System.out.println(correctPercent);
        ModelAndView modelAndView = new ModelAndView("practice/result");
        Optional<Player> playerOptional = this.playerService.findById(idPlayer);
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            player.setCorrectPercent(correctPercent);
            this.playerService.savePlayer(player);
            modelAndView.addObject("score", player.getPoint());
            modelAndView.addObject("correctPercent", correctPercent);
        }
        return modelAndView;
    }

}
