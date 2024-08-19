package com.webapp.madrasati.auth.model.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDto {
    private String accessToken;

    private String token;

    Instant expiryDate;

    private LoginUserDto user;

}
