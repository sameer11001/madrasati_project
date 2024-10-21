package com.webapp.madrasati.auth.model.dto.res;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginGuestResponseDto {

    UUID Gid;
    
    String username;

    private String accessToken;

    private String token;

    Instant expiryDate;
}
