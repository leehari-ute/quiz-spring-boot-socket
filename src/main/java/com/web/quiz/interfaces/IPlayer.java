package com.web.quiz.interfaces;

import com.web.quiz.models.Game;
import com.web.quiz.models.Player;

import java.util.List;
import java.util.Optional;

public interface IPlayer {
    Player savePlayer(Player player);
    List<Player> findByIdGame(Game game);
    Optional<Player> findById(String id);
    void deletePlayer(Player player);
    Optional<Player> findByIdGameAndSessionId(Game game, String sessionId);
    List<Player> findByIdGameAndStatus(Game game, String status);
    Optional<Player> findBySessionIdAndStatus(String sessionId, String status);
    List<Player> findByIdGameAndStatusOrderByPointDesc(Game idGame, String status);
    List<Player> findByIdGameOrderByPointDesc(Game idGame);
    void deleteByIdGameAndStatus(Game idGame, String status);
}
