package com.web.quiz.services;

import com.web.quiz.constant.Role;
import com.web.quiz.interfaces.IUser;
import com.web.quiz.models.User;
import com.web.quiz.repositories.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUser {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public List<String> getAllUsername() {
        return this.userRepository.getAllUsername();
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return this.userRepository.findUserByUsername(username);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return this.userRepository.findUserByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public String redirectBaseRole(String role) {
        if (role.equalsIgnoreCase(Role.TEACHER))
            return "redirect:/dashboard";
        return "redirect:/home";
    }
    @Override
    public String getRoleUser(Optional<User> user) {
        return user.map(value -> value.getRole().toUpperCase()).orElse(Role.ANONYMOUS);
    }

    @Override
    public Optional<User> getUserByAuthentication(Authentication authentication) {
        if (authentication == null)
            return Optional.empty();
        JSONObject principal = new JSONObject(authentication.getPrincipal());
        try {
            String email = principal.get("email").toString();
            return getUserByEmail(email);
        } catch (JSONException e) {
            String username = principal.get("username").toString();
            return getUserByUsername(username);
        }
    }
}
