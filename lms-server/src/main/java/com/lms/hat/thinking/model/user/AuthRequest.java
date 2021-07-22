package com.lms.hat.thinking.model.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthRequest {
    @NotBlank(message = "login")
    private String login;

    @NotBlank(message = "password")
    private String password;
}
