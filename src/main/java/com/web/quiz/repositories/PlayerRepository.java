package com.web.quiz.repositories;

import com.web.quiz.models.Game;
import com.web.quiz.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, String> {
    List<Player> findByIdGame(Game idGame);
    Optional<Player> findByIdGameAndSessionId(Game idGame, String sessionId);
    List<Player> findByIdGameAndStatus(Game idGame, String status);
    Optional<Player> findBySessionIdAndStatus(String sessionId, String status);
    List<Player> findByIdGameAndStatusOrderByPointDesc(Game idGame, String status);
    List<Player> findByIdGameOrderByPointDesc(Game idGame);
    void deleteByIdGameAndStatus(Game idGame, String status);
}
