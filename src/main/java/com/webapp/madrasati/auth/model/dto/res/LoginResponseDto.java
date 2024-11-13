package com.webapp.madrasati.auth.model.dto.res;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {

    private String userId;

    private String accessToken;

    private String token;

    Instant expiryDate;

    private UserResDto user;

    private Object data;

}
