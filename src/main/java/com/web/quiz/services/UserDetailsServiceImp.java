package com.web.quiz.services;

import com.web.quiz.models.User;
import com.web.quiz.models.UserDetailsImp;
import com.web.quiz.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImp implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));
        return new UserDetailsImp(user);
    }
}
