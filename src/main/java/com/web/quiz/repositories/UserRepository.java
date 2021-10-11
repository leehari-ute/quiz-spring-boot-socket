package com.web.quiz.repositories;

import com.web.quiz.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmail(String email);
    @Query("select username from User")
    List<String> getAllUsername();
}
