package com.web.quiz.interfaces;

import com.web.quiz.models.User;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface IUser {
    List<User> findAll();
    List<String> getAllUsername();
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);
    User saveUser(User user);
    Optional<User> getUserByAuthentication(Authentication authentication);
    String redirectBaseRole(String role);
    String getRoleUser(Optional<User> user);
}
