package com.webapp.madrasati.auth.model.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponseDto {
    private String accessToken;

    private String token;

    Instant expiryDate;
}
