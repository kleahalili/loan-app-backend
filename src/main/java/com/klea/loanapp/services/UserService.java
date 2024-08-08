package com.klea.loanapp.services;

import com.klea.loanapp.entities.User;
import com.klea.loanapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public void updateLastLoginTime(String email) {
        Date lastLoginDate = java.sql.Timestamp.valueOf(LocalDateTime.now());

        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        user.setLastLoginTime(lastLoginDate);
        userRepository.save(user);
    }
}
