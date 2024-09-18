package com.klea.loanapp.services;

import com.klea.loanapp.entities.PasswordResetToken;
import com.klea.loanapp.entities.User;
import com.klea.loanapp.repositories.PasswordResetTokenRepository;
import com.klea.loanapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    public void updateLastLoginTime(String email) {
        User user = findByEmail(email);
        user.setLastLoginTime(java.sql.Timestamp.valueOf(LocalDateTime.now()));
        userRepository.save(user);
    }

    public void generateResetToken(String email) {
        User user = findByEmail(email);

        // Create a new password reset token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(calculateExpiryDate(60)); // 1 hour expiry
        passwordResetTokenRepository.save(resetToken);

        // Send the reset link via email
        String resetLink = "http://yourfrontend.com/reset-password?token=" + token;
        sendEmailService.sendPasswordResetEmail(user.getEmail(), "Password Reset Request",
                "Click the link to reset your password: " + resetLink);
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return calendar.getTime();
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (passToken.getExpiryDate().before(new Date())) {
            throw new IllegalArgumentException("Token has expired");
        }

        User user = passToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Optionally, delete the token after successful reset
        passwordResetTokenRepository.delete(passToken);
    }
}
