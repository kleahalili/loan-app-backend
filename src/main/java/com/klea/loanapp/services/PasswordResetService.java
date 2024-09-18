package com.klea.loanapp.services;

import com.klea.loanapp.entities.PasswordResetToken;
import com.klea.loanapp.entities.User;
import com.klea.loanapp.repositories.PasswordResetTokenRepository;
import com.klea.loanapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final int EXPIRATION = 60 * 24; // Token expiry time in minutes (24 hours)

    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDate(calculateExpiryDate(EXPIRATION));
        passwordResetTokenRepository.save(passwordResetToken);
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return calendar.getTime();
    }

    public void generateResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user found with this email"));

        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(user, token);

        String resetLink = "http://yourfrontend.com/reset-password?token=" + token;
        String body = "Click the link to reset your password: " + resetLink;
        sendEmailService.sendPasswordResetEmail(user.getEmail(), "Password Reset Request", body);
    }

    public void validatePasswordResetToken(String token) {
        PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (passToken.getExpiryDate().before(new Date())) {
            throw new IllegalArgumentException("Token has expired");
        }
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        validatePasswordResetToken(token);

        User user = passToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Optionally, remove the used token
        passwordResetTokenRepository.delete(passToken);
    }
}
