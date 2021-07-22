package com.lms.hat.thinking.controller;

import com.lms.hat.thinking.config.security.services.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestSecurityController {

    @GetMapping("/admin/get")
    public String getAdmin() {
        return "Hi admin";
    }

    @GetMapping("/user/getUser")
    public String getUser(Authentication authentication) {
        CustomUserDetails cud = (CustomUserDetails)authentication.getPrincipal();
        return cud.getUsername();
    }
}
