package com.web.quiz.services;

import com.web.quiz.interfaces.IPlayer;
import com.web.quiz.models.Game;
import com.web.quiz.models.Player;
import com.web.quiz.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService implements IPlayer {
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Player savePlayer(Player player) {
        return this.playerRepository.save(player);
    }

    @Override
    public List<Player> findByIdGame(Game game) {
        return this.playerRepository.findByIdGame(game);
    }

    @Override
    public Optional<Player> findById(String id) {
        return this.playerRepository.findById(id);
    }

    @Override
    public void deletePlayer(Player player) {
        this.playerRepository.delete(player);
    }

    @Override
    public Optional<Player> findByIdGameAndSessionId(Game game, String sessionId) {
        return this.playerRepository.findByIdGameAndSessionId(game, sessionId);
    }

    @Override
    public List<Player> findByIdGameAndStatus(Game game, String status) {
        return this.playerRepository.findByIdGameAndStatus(game, status);
    }

    @Override
    public Optional<Player> findBySessionIdAndStatus(String sessionId, String status) {
        return this.playerRepository.findBySessionIdAndStatus(sessionId, status);
    }

    @Override
    public List<Player> findByIdGameAndStatusOrderByPointDesc(Game idGame, String status) {
        return this.playerRepository.findByIdGameAndStatusOrderByPointDesc(idGame, status);
    }

    @Override
    public List<Player> findByIdGameOrderByPointDesc(Game idGame) {
        return this.playerRepository.findByIdGameOrderByPointDesc(idGame);
    }

    @Override
    public void deleteByIdGameAndStatus(Game idGame, String status) {
        this.playerRepository.deleteByIdGameAndStatus(idGame, status);
    }


}
