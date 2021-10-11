package com.web.quiz.interfaces;

import com.web.quiz.models.Game;
import com.web.quiz.models.User;

import java.util.Optional;

public interface IGame {
    Game saveGame(Game game);
    Optional<Game> findByCodeAndStatus(String code, String status);
    Optional<Game> findById(Integer id);
    Optional<Game> findByIdAndStatus(Integer id, String status);
    Optional<Game> findByIdAndStatusAndIdHost(Integer id, String status, User idHost);
}
