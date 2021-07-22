package com.lms.hat.thinking.model.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class RegistrationRequest {
    @NotBlank(message = "man123321")
    private String login;

    @NotBlank(message = "email@gmail.com")
    @Email
    private String email;

    @NotBlank(message = "securepass")
    private String password;
}
