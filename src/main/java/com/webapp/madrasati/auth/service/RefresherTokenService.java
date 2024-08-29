package com.webapp.madrasati.auth.service;

import java.time.Instant;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.webapp.madrasati.auth.error.RefresherTokenExpired;
import com.webapp.madrasati.auth.model.RefresherToken;
import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.model.dto.res.JwtResponseDto;
import com.webapp.madrasati.auth.repository.RefresherTokenRepostiory;
import com.webapp.madrasati.auth.security.JwtTokenUtils;
import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.error.BadRequestException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.core.model.ApiResponse;

@Service
public class RefresherTokenService {

    private RefresherTokenRepostiory refresherTokenRepository;

    private JwtTokenUtils jwtTokenUtils;

    private Long REFRESH_TOKEN_VALIDITY;

    public RefresherTokenService(RefresherTokenRepostiory refresherTokenRepository,
            @Value("${jwt.refresher.time}") Long refreshTokenValidity, JwtTokenUtils jwtTokenUtils) {
        this.refresherTokenRepository = refresherTokenRepository;
        REFRESH_TOKEN_VALIDITY = refreshTokenValidity;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    public RefresherToken findByToken(String token) {
        return refresherTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException(token + " Refresher token not found!"));
    }

    public RefresherToken createRefreshToken(UserEntity user) {

        RefresherToken refreshToken = RefresherToken.builder().user(
                user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(REFRESH_TOKEN_VALIDITY)) // 7 days
                // configure it application.properties file
                .build();
        if (existsByToken(refreshToken.getToken())) {
            throw new BadRequestException("Already Login");
        }
        return refresherTokenRepository.save(refreshToken);
    }

    public boolean existsByToken(String token) {
        return refresherTokenRepository.existsByToken(token);
    }

    public boolean verifyExpiration(RefresherToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refresherTokenRepository.delete(token);
            throw new RefresherTokenExpired(token.getToken());
        }
        return true;
    }

    public void deleteByToken(String token) {
        RefresherToken refreshToken = refresherTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException(token + " Refresh token not found in DB"));
        refresherTokenRepository.deleteById(refreshToken.getId());

    }

    public String generateAccessToken(String username) {
        return jwtTokenUtils.generateToken(username);
    }

    public ApiResponse<JwtResponseDto> refreshToken(String token) {
        RefresherToken refreshToken = refresherTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException(token + " Refresher token not found!"));

        if (verifyExpiration(refreshToken)) {
            String accessToken = generateAccessToken(refreshToken.getUser().getUserEmail());
            LoggerApp.debug("Generated new access token successfully: ", accessToken);
            JwtResponseDto response = JwtResponseDto.builder()
                    .accessToken(accessToken)
                    .token(token)
                    .expiryDate(refreshToken.getExpiryDate())
                    .build();
            return ApiResponse.success(response, "Token refreshed successfully", HttpStatus.OK);
        }

        return null;
    }
}
