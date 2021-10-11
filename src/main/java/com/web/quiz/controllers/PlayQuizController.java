package com.web.quiz.controllers;

import com.web.quiz.models.*;
import com.web.quiz.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class PlayQuizController {
    private final GameService gameService;
    private final PlayerService playerService;
    private final QuizService quizService;
    private final AuthService authService;
    private final UserService userService;
    private final QuestionService questionService;

    @Autowired
    public PlayQuizController(GameService gameService,QuestionService questionService, UserService userService, AuthService authService, PlayerService playerService, QuizService quizService) {
        this.playerService = playerService;
        this.gameService = gameService;
        this.quizService = quizService;
        this.authService = authService;
        this.userService = userService;
        this.questionService = questionService;
    }

    @MessageMapping("/add-player/{idGame}")
    @SendTo("/play/public-room/{idGame}")
    public Message addPlayer(Message message, SimpMessageHeaderAccessor headerAccessor) throws NullPointerException {
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("player", message.getSender());
        return message;
    }

    @MessageMapping("/send-start/{idGame}")
    @SendTo("/play/public-room/{idGame}")
    public Message sendMessage(Message message, @DestinationVariable Integer idGame) {
        Optional<Game> gameOptional = this.gameService.findById(idGame);
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            this.playerService.deleteByIdGameAndStatus(game, "out");
            game.setStatus("playing");
            this.gameService.saveGame(game);
            Optional<Quiz> quiz = this.quizService.findById(Integer.parseInt(message.getContent().toString()));
            if (quiz.isPresent()) {
                List<Integer> listQuestionId = this.questionService.getListQuestionIdByQuiz(quiz.get());
                message.setContent(listQuestionId);
            }
        }
        return message;
    }

    @MessageMapping("/send-answer/{idGame}")
    @SendTo("/play/public-room/{idGame}")
    public Message sendAnswer(Message message) {
        Optional<Player> playerOptional = this.playerService.findById(message.getSender().getId());
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            player.setPoint(message.getSender().getPoint());
            Player playerSaved = this.playerService.savePlayer(player);
            message.setSender(new Message.Sender(playerSaved.getName(), playerSaved.getId(), playerSaved.getPoint()));
        }
        return message;
    }

    @MessageMapping("/send-leave/{idGame}")
    @SendTo("/play/public-room/{idGame}")
    public Message sendLeave(Message message) {
        Optional<Player> playerOptional = this.playerService.findById(message.getSender().getId());
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            player.setStatus("out");
            Player playerSaved = this.playerService.savePlayer(player);
            message.setSender(new Message.Sender(playerSaved.getName(), playerSaved.getId(), playerSaved.getPoint()));
        }
        return message;
    }

    @MessageMapping("/send-end/{idGame}")
    @SendTo("/play/public-room/{idGame}")
    public Message sendEnd(Message message) {
        Optional<Player> playerOptional = this.playerService.findById(message.getSender().getId());
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            player.setStatus("completed");
            this.playerService.savePlayer(player);
        }
        return message;
    }

    @GetMapping("/join/{idGame}/prepare-game")
    public String getViewPlayJoin(@PathVariable Integer idGame, Model model) {
        Optional<Game> game = this.gameService.findByIdAndStatus(idGame, "waiting");
        if (game.isPresent()) {
            model.addAttribute("game", game.get());
            return "/play/prepare";
        }
        return "redirect:/error/404";
    }

    @GetMapping("/host/game/{idGame}")
    public String getViewPlayHost(Model model, @PathVariable Integer idGame, Authentication authentication) {
        if (this.authService.isAuth()) {
            Optional<User> user = this.userService.getUserByAuthentication(authentication);
            if (user.isPresent()) {
                Optional<Game> game = this.gameService.findByIdAndStatusAndIdHost(idGame, "waiting", user.get());
                if (game.isPresent()) {
                    model.addAttribute("game", game.get());
                    List<Player> playerList = this.playerService.findByIdGame(game.get());
                    model.addAttribute("players", playerList);
                    return "/play/host";
                }
            }
        }
        return "redirect:/error/404";
    }
}
