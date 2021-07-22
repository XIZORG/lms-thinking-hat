package com.lms.hat.thinking.config.security.services;


import com.lms.hat.thinking.exception.model.FieldErrorModel;
import com.lms.hat.thinking.exception.status.RestConflictException;
import com.lms.hat.thinking.model.user.RefreshToken;
import com.lms.hat.thinking.repository.RefreshTokenRepository;
import com.lms.hat.thinking.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private Long refreshTokenDurationMs = 200000000l;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserEntityRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RestConflictException(Collections.singletonList(
                    new FieldErrorModel("token", HttpStatus.CONFLICT.getReasonPhrase(),
                            "refresh token was expired, try to sign in!")));
        }

        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }

    @Transactional
    public int deleteByUserName(String login) {
        return refreshTokenRepository.deleteByUser(userRepository.findByLogin(login));
    }
}
