package com.webapp.madrasati.auth.service;

import com.webapp.madrasati.auth.model.dto.req.LoginRequestDto;
import com.webapp.madrasati.auth.model.dto.res.JwtResponseDto;

public interface AuthenticateService {
    JwtResponseDto login(LoginRequestDto requestBody);

    Void logout(String token);
}
