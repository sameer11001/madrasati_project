package com.webapp.madrasati.auth.service;

import com.webapp.madrasati.auth.model.dto.req.LoginRequestDto;
import com.webapp.madrasati.auth.model.dto.res.JwtResponseDto;
import com.webapp.madrasati.auth.model.dto.res.LoginGuestResponseDto;

public interface AuthenticateService {
    JwtResponseDto login(LoginRequestDto requestBody, String deviceId);

    void logout(String token);

    LoginGuestResponseDto guestLogin (String deviceId);

    void guestLogout(String token, String userIdString);
}
