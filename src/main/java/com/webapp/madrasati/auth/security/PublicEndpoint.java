package com.webapp.madrasati.auth.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PublicEndpoint {

    PublicEndpoint() {}
    protected static final Set<String> endpoints = new HashSet<>(Arrays.asList(
            "/", "/v1/auth/login",
            "/v1/auth/logout", "/v1/auth/refreshToken", "/v1/auth/guestLogin",
            "/v1/auth/guestLogout", "/static/**","/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**"));

}
