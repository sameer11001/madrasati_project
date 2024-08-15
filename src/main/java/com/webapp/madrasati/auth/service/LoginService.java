package com.webapp.madrasati.auth.service;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.webapp.madrasati.auth.model.RefresherToken;
import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.model.dto.request.LoginRequestDto;
import com.webapp.madrasati.auth.model.dto.response.JwtResponseDto;
import com.webapp.madrasati.auth.security.JwtTokenUtils;
import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.model.ApiResponse;

@Service
public class LoginService {

    private UserService userService;

    private AuthenticationManager authenticationManager;

    private RefresherTokenService refresherTokenService;

    private JwtTokenUtils jwtTokenUtils;

    LoginService(RefresherTokenService refresherToken, UserService userService,
            AuthenticationManager authenticationManager, JwtTokenUtils jwtTokenUtils) {
        this.refresherTokenService = refresherToken;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    public ApiResponse<JwtResponseDto> login(LoginRequestDto requestBody) throws AuthenticationException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestBody.getUserEmail(), requestBody.getPassword()));
        Optional<UserEntity> user = userService.findByUserEmail(requestBody.getUserEmail());
        if (user.isPresent() && authentication.isAuthenticated()) {
            LoggerApp.info("doAuthenticate complete with " + requestBody.getUserEmail());

            // Security Best Practices
            SecurityContextHolder.getContext().setAuthentication(authentication); // Optional based on framework
            LoggerApp.info("setAuthentication complete with " + requestBody.getUserEmail());

            // refresher token generation
            RefresherToken refresherToken = refresherTokenService.createRefreshToken(user.get());

            // jwt access token generation
            String accessToken = jwtTokenUtils.generateToken(user.get().getUserEmail());

            return ApiResponse.success(JwtResponseDto.builder()
                    .accessToken(accessToken)
                    .token(refresherToken.getToken())
                    .build(), "Login Successful");
        }
        throw new BadCredentialsException("Invalid username or password");
    }

}
