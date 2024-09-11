package com.webapp.madrasati.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.madrasati.auth.model.dto.req.LoginRequestDto;
import com.webapp.madrasati.auth.model.dto.res.JwtResponseDto;
import com.webapp.madrasati.auth.service.AuthenticateService;
import com.webapp.madrasati.auth.service.RefresherTokenService;
import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.model.ApiResponseBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    AuthenticateService authenticateService;

    RefresherTokenService refresherTokenService;

    AuthController(AuthenticateService authenticateService, RefresherTokenService refresherTokenService) {
        this.authenticateService = authenticateService;
        this.refresherTokenService = refresherTokenService;
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated", content = @Content(schema = @Schema(implementation = JwtResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ApiResponseBody<JwtResponseDto> login(
            @Parameter(description = "Login credentials", required = true) @RequestBody @Valid LoginRequestDto requestBody) {
        return authenticateService.login(requestBody);
    }

    @PostMapping("/token")
    @Operation(summary = "Refresh token", description = "Refreshes an existing JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token successfully refreshed", content = @Content(schema = @Schema(implementation = JwtResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid token"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ApiResponseBody<JwtResponseDto> refreshToken(
            @Parameter(description = "Refresh token", required = true) @RequestHeader("refresher-token") String token) {
        LoggerApp.debug("Refresh token: ", token);
        return refresherTokenService.refreshToken(token);
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Logs out a user and invalidates the refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged out"),
            @ApiResponse(responseCode = "400", description = "Invalid token"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ApiResponseBody<Void> logout(
            @Parameter(description = "Refresh token", required = true) @RequestHeader("refresher-token") String token) {
        return authenticateService.logout(token);
    }
}
