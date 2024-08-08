package com.klea.loanapp.services;

import lombok.*;

@Setter
@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyUserRequest {
    // Getters and setters
    private String newEmail;
    private String newPassword;
}