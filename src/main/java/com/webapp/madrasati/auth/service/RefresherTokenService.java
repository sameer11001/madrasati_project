package com.webapp.madrasati.auth.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.webapp.madrasati.auth.error.RefresherTokenExpired;
import com.webapp.madrasati.auth.model.RefresherToken;
import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.repository.RefresherTokenRepostiory;
import com.webapp.madrasati.auth.security.JwtTokenUtils;
import com.webapp.madrasati.core.error.ResourceNotFoundException;

@Service
public class RefresherTokenService {

    private RefresherTokenRepostiory refresherTokenRepostiory;

    private JwtTokenUtils jwtTokenUtils;

    private Long REFRESH_TOKEN_VALIDITY;

    public RefresherTokenService(RefresherTokenRepostiory refresherTokenRepostiory,
            @Value("${jwt.refresher.time}") Long refreshTokenValidity, JwtTokenUtils jwtTokenUtils) {
        this.refresherTokenRepostiory = refresherTokenRepostiory;
        REFRESH_TOKEN_VALIDITY = refreshTokenValidity;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    public Optional<RefresherToken> findByToken(String token) {

        if (!refresherTokenRepostiory.findByToken(token).isPresent()) {
            throw new ResourceNotFoundException(token + "Refresher token not found!");
        }
        return refresherTokenRepostiory.findByToken(token);
    }

    public RefresherToken createRefreshToken(UserEntity user) {
        RefresherToken refreshToken = RefresherToken.builder().user(
                user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(REFRESH_TOKEN_VALIDITY)) // 7 days
                // configure it application.properties file
                .build();
        return refresherTokenRepostiory.save(refreshToken);
    }

    public boolean existsByToken(String token) {
        if (!refresherTokenRepostiory.existsByToken(token)) {
            throw new ResourceNotFoundException(token + "Refresher token not found!");
        }

        return refresherTokenRepostiory.existsByToken(token);
    }

    public boolean verifyExpiration(RefresherToken token) {

        if (!refresherTokenRepostiory.findByToken(token.getToken()).isPresent()) {
            throw new ResourceNotFoundException(token + "Refresher token not found!");
        }
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refresherTokenRepostiory.delete(token);
            throw new RefresherTokenExpired(token.getToken());
        }
        return true;
    }

    public void deleteByToken(String token) {
        Optional<RefresherToken> refreshToken = refresherTokenRepostiory.findByToken(token);
        if (refreshToken.isPresent()) {
            refresherTokenRepostiory.deleteById(refreshToken.get().getId());
        } else {
            throw new ResourceNotFoundException(token + " Refresh token not found in DB");
        }

    }

    public String generateAccessToken(String username) {
        return jwtTokenUtils.generateToken(username);
    }
}
