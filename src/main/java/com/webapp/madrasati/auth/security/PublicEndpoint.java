package com.webapp.madrasati.auth.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PublicEndpoint {
    private PublicEndpoint() {}
    protected static final Set<String> endpoints = new HashSet<>(
            Arrays.asList("/v3/api-docs/**", "/swagger-resources/**", "/swagger-resources",
                    "/swagger-ui/**", "/swagger-ui.html", "/", "/v1/auth/login",
                    "/v1/auth/logout", "/v1/auth/refreshToken", "/v1/auth/guestLogin",
                    "/v1/auth/guestLogout", "/static/**"));
}

