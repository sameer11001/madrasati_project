package com.webapp.madrasati.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.madrasati.auth.model.dto.request.LoginRequestDto;
import com.webapp.madrasati.auth.model.dto.response.JwtResponseDto;
import com.webapp.madrasati.auth.service.LoginService;
import com.webapp.madrasati.core.model.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public ApiResponse<JwtResponseDto> login(@RequestBody @Valid LoginRequestDto requestBody) {
        return loginService.login(requestBody);
    }
}
