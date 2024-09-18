package com.klea.loanapp.repositories;

import com.klea.loanapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
//    Optional<User> findByResetPasswordToken(String token);
}
