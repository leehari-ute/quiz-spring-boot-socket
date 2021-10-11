package com.web.quiz.repositories;

import com.web.quiz.models.Game;
import com.web.quiz.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Integer> {
    Optional<Game> findByCodeAndStatus(String code, String status);
    Optional<Game> findByIdAndStatus(Integer id, String status);
    Optional<Game> findByIdAndStatusAndIdHost(Integer id, String status, User idHost);
}
