package com.webapp.madrasati.auth.service;

import java.time.Instant;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.madrasati.auth.error.RefresherTokenExpired;
import com.webapp.madrasati.auth.model.RefresherToken;
import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.model.dto.res.JwtResponseDto;
import com.webapp.madrasati.auth.repository.RefresherTokenRepostiory;
import com.webapp.madrasati.auth.security.JwtTokenUtils;
import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.error.BadRequestException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;

@Service
@Transactional(rollbackFor = { ResourceNotFoundException.class, BadRequestException.class })
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

    public boolean existsByDeviceId(String deviceId) {
        return refresherTokenRepository.existsByDeviceId(deviceId);
    }

    public RefresherToken createRefreshToken(UserEntity user, String deviceId) {

        RefresherToken refreshToken = RefresherToken.builder().user(
                user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(REFRESH_TOKEN_VALIDITY)) // 7 days
                .deviceId(deviceId)
                .build();
        if (existsByToken(refreshToken.getToken())) {
            throw new BadRequestException("Already Login");
        }
        return refresherTokenRepository.save(refreshToken);
    }

    public boolean existsByToken(String token) {
        return refresherTokenRepository.existsByToken(token);
    }

    public boolean existsByUser(UserEntity user) {
        return refresherTokenRepository.existsByUser(user);
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
                .orElseThrow(() -> new BadCredentialsException(" you are not login!"));

        refresherTokenRepository.deleteById(refreshToken.getId());

    }

    public String generateAccessToken(String username, UUID id) {
        return jwtTokenUtils.generateToken(username, id);
    }

    public JwtResponseDto refreshToken(String token) {
        RefresherToken refreshToken = refresherTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException(token + " Refresher token not found!"));

        if (verifyExpiration(refreshToken)) {
            String accessToken = generateAccessToken(refreshToken.getUser().getUserEmail(),
                    refreshToken.getUser().getId());
            LoggerApp.debug("Generated new access token successfully: ", accessToken);
            return JwtResponseDto.builder()
                    .accessToken(accessToken)
                    .token(token)
                    .expiryDate(refreshToken.getExpiryDate())
                    .build();
        }

        return null;
    }
}

// TODO IMPLEMENT NEW Feature AT REFRESH TOKEN