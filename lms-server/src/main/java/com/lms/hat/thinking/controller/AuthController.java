package com.lms.hat.thinking.controller;


import com.lms.hat.thinking.config.security.jwt.JwtUtils;
import com.lms.hat.thinking.config.security.services.CustomUserDetails;
import com.lms.hat.thinking.config.security.services.RefreshTokenService;
import com.lms.hat.thinking.exception.model.FieldErrorModel;
import com.lms.hat.thinking.exception.status.RestConflictException;
import com.lms.hat.thinking.model.response.JwtResponse;
import com.lms.hat.thinking.model.token.TokenRefreshRequest;
import com.lms.hat.thinking.model.token.TokenRefreshResponse;
import com.lms.hat.thinking.model.user.*;
import com.lms.hat.thinking.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    private final UserServiceImpl userService;

    @Autowired
    public AuthController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid  @RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(userService.saveUser(registrationRequest));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody AuthRequest loginRequest) {
        refreshTokenService.deleteByUserName(loginRequest.getLogin());
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), roles));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getLogin());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new RestConflictException(Collections.singletonList(
                new FieldErrorModel("token", HttpStatus.CONFLICT.getReasonPhrase(),
                        "Refresh token is not in database!"))));
    }


    @PostMapping("/log-out")
    public ResponseEntity<?> logoutUser(Authentication authentication) {
        CustomUserDetails cud = (CustomUserDetails)authentication.getPrincipal();
        refreshTokenService.deleteByUserId(cud.getId());
        return ResponseEntity.ok("Log out successful!");
    }
}
