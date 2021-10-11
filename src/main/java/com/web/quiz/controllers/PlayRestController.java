package com.web.quiz.controllers;

import com.web.quiz.models.*;
import com.web.quiz.services.*;
import com.web.quiz.utils.HelperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/socket")
public class PlayRestController {
    private final GameService gameService;
    private final QuizService quizService;
    private final PlayerService playerService;
    private final QuestionService questionService;
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public PlayRestController(GameService gameService, AuthService authService, QuizService quizService, PlayerService playerService, QuestionService questionService, UserService userService) {
        this.gameService = gameService;
        this.quizService = quizService;
        this.playerService = playerService;
        this.questionService = questionService;
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/get-prepare/join")
    public ModelAndView getViewPrepare(@RequestParam("idGame") Integer idGame, @RequestParam("name") String name) {
        ModelAndView modelAndView = new ModelAndView("play/join");
        Optional<Game> game = this.gameService.findById(idGame);
        if (game.isPresent()) {
            String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
//            System.out.println(sessionId);
            Optional<Player> playerOptional = this.playerService.findByIdGameAndSessionId(game.get(), sessionId);
            Player player = new Player();
            if (playerOptional.isPresent()) {
                // Tim cach xu ly neu do bang 2 tab
                player = playerOptional.get();
            } else {
                player.setIdGame(game.get());
                player.setSessionId(sessionId);
                String idPlayer = HelperUtils.generateID(10);
                while (this.playerService.findById(idPlayer).isPresent()) {
                    idPlayer = HelperUtils.generateID(10);
                }
                player.setId(idPlayer);
            }
            player.setPoint(0);
            player.setStatus("in");
            player.setName(name);
            player.setCorrectPercent(0);
            if (this.authService.isAuth()) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            System.out.println(auth);
                Optional<User> user = this.userService.getUserByAuthentication(auth);
                if (user.isPresent()) {
//                System.out.println(user.get());
                    player.setIdUser(user.get());
                }
            }
            Player playerSaved = this.playerService.savePlayer(player);
            List<Player> playerList = this.playerService.findByIdGameAndStatus(game.get(), "in");
            modelAndView.addObject("game", game.get());
            modelAndView.addObject("name", name);
            modelAndView.addObject("idPlayer", playerSaved.getId());
            modelAndView.addObject("players", playerList);
        }
        return modelAndView;
    }

    @PostMapping("/render-code/{idQuiz}")
    public Response renderCode(@PathVariable Integer idQuiz, Authentication authentication) {
        if (this.authService.isAuth()) {
            Optional<User> userOptional = this.userService.getUserByAuthentication(authentication);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                Optional<Quiz> quiz = this.quizService.findByIdAndIdOwnerAndIsPublish(idQuiz, user,true);
                if (quiz.isPresent()) {
                    String code = HelperUtils.generateID(6);
                    while (this.gameService.findByCodeAndStatus(code, "waiting").isPresent()) {
                        code = HelperUtils.generateID(6);
                    }
                    Game game = new Game();
                    game.setCode(code);
                    game.setIdQuiz(quiz.get());
                    game.setStatus("waiting");
                    game.setIdHost(user);
                    Game gameSaved = this.gameService.saveGame(game);
                    return new Response("Successfully", 200, true, gameSaved);
                }
            }
        }
        return new Response("Error", 500, false, "");
    }

    @GetMapping("/get-question/{idQuiz}/{idQues}/{idPlayer}")
    public ModelAndView getQuestion(@PathVariable Integer idQues, @PathVariable Integer idQuiz, @PathVariable String idPlayer) {
        ModelAndView modelAndView = new ModelAndView("play/question");
        Optional<Question> question = this.questionService.findByPrimaryKey(new Question.PrimaryKey(idQues, idQuiz));
        if (question.isPresent()) {
            Optional<Player> player = this.playerService.findById(idPlayer);
            player.ifPresent(value -> modelAndView.addObject("player", value));
            modelAndView.addObject("question", question.get());
        }
        return modelAndView;
    }

    @GetMapping("/get-play/join")
    public ModelAndView getViewPlay() {
        return new ModelAndView("play/in-game");
    }

    @GetMapping("/find-game/{code}")
    public Response findGame(@PathVariable String code) {
        Optional<Game> game = this.gameService.findByCodeAndStatus(code, "waiting");
        return game.map(value -> new Response("Successfully", 200, true, value)).orElseGet(() -> new Response("Error", 500, false, ""));
    }

    @GetMapping("/get-leaderboard/{idGame}")
    public ModelAndView getLeaderboard(@PathVariable Integer idGame) {
        Optional<Game> game = this.gameService.findById(idGame);
        if (game.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("play/leaderboard");
            List<Player> players = this.playerService.findByIdGameOrderByPointDesc(game.get());
            modelAndView.addObject("players", players);
            return modelAndView;
        }
        return new ModelAndView();
    }
}
