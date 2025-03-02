package com.webapp.madrasati.auth.controller;

import com.webapp.madrasati.auth.model.dto.res.RefreshTokenResponseDto;
import com.webapp.madrasati.auth.service.AuthenticateService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.madrasati.auth.model.dto.req.GuestLogoutReqDto;
import com.webapp.madrasati.auth.model.dto.req.LoginRequestDto;
import com.webapp.madrasati.auth.model.dto.res.LoginResponseDto;
import com.webapp.madrasati.auth.model.dto.res.LoginGuestResponseDto;
import com.webapp.madrasati.auth.service.RefresherTokenService;
import com.webapp.madrasati.core.model.ApiResponseBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("v1/auth")
public class AuthController {

        private final AuthenticateService authenticateService;

        private final RefresherTokenService refresherTokenService;

        @PostMapping("login")
        @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully authenticated", content = @Content(schema = @Schema(implementation = LoginResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid credentials"),
                        @ApiResponse(responseCode = "401", description = "Unauthorized")
        })
        @ResponseStatus(HttpStatus.OK)
        public ApiResponseBody<LoginResponseDto> login(
                        @Parameter(description = "Login credentials", required = true) @RequestBody @Valid LoginRequestDto requestBody,
                        @Parameter(name = "device-id", description = "The unique device ID of the client making the request", required = true) @NotEmpty(message = "Device ID must not be empty") @NotBlank @RequestHeader("device-id") String deviceId) {
                return ApiResponseBody.success(authenticateService.login(requestBody,
                                deviceId), "Login Successful", HttpStatus.OK);
        }

        @PostMapping("guestLogin")
        @Operation(summary = "Guest login", description = "Authenticates a guest user and returns a limited JWT token")
        @ResponseStatus(HttpStatus.OK)
        public ApiResponseBody<LoginGuestResponseDto> guestLogin(@NotEmpty(message = "Device ID must not be empty") @NotBlank  @RequestHeader("device-id") String deviceId) {
                return ApiResponseBody.success(authenticateService.guestLogin(deviceId), "Guest Login Successful",
                                HttpStatus.OK);
        }

        @PostMapping("refreshToken")
        @Operation(summary = "Refresh token", description = "Refreshes an existing JWT token")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Token successfully refreshed", content = @Content(schema = @Schema(implementation = LoginResponseDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid token"),
                        @ApiResponse(responseCode = "401", description = "Unauthorized")
        })
        @ResponseStatus(HttpStatus.OK)
        public ApiResponseBody<RefreshTokenResponseDto> refreshToken(
                        @Parameter(description = "Refresh token", required = true) @NotEmpty(message = "refresh token must not be empty") @NotBlank @RequestHeader("refresher-token") String token) {
                return ApiResponseBody.success(refresherTokenService.refreshToken(token), "Token refreshed",
                                HttpStatus.OK);
        }

        @PostMapping("logout")
        @Operation(summary = "User logout", description = "Logs out a user and invalidates the refresh token")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Successfully logged out"),
                        @ApiResponse(responseCode = "400", description = "Invalid token"),
                        @ApiResponse(responseCode = "401", description = "Unauthorized")
        })
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public ApiResponseBody<Void> logout(
                        @Parameter(description = "Refresh token", required = true) @NotEmpty(message = "refresh token must not be empty") @NotBlank @RequestHeader("refresher-token") String token) {
                authenticateService.logout(token);
                return ApiResponseBody.successWithNoData;
        }

        @PostMapping("guestLogout")
        @Operation(summary = "Guest logout", description = "Logs out a guest user and invalidates the refresh token")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Successfully logged out"),
                        @ApiResponse(responseCode = "400", description = "Invalid token"),
                        @ApiResponse(responseCode = "404", description = "You Are Not Logged In!")
        })
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public ApiResponseBody<Void> guestLogout(
                        @Parameter(description = "Refresh token", required = true, schema = @Schema(implementation = GuestLogoutReqDto.class)) @Valid @RequestBody GuestLogoutReqDto requestBody) {
                authenticateService.guestLogout(requestBody.getRefreshToken(), requestBody.getGuid());
                return ApiResponseBody.successWithNoData;
        }
}
