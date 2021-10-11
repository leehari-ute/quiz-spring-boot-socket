package com.web.quiz.services;

import com.web.quiz.interfaces.IGame;
import com.web.quiz.models.Game;
import com.web.quiz.models.User;
import com.web.quiz.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameService implements IGame {
    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Game saveGame(Game game) {
        return this.gameRepository.save(game);
    }

    @Override
    public Optional<Game> findByCodeAndStatus(String code, String status) {
        return this.gameRepository.findByCodeAndStatus(code, status);
    }

    @Override
    public Optional<Game> findById(Integer id) {
        return this.gameRepository.findById(id);
    }

    @Override
    public Optional<Game> findByIdAndStatus(Integer id, String status) {
        return this.gameRepository.findByIdAndStatus(id, status);
    }

    @Override
    public Optional<Game> findByIdAndStatusAndIdHost(Integer id, String status, User idHost) {
        return this.gameRepository.findByIdAndStatusAndIdHost(id, status, idHost);
    }
}
