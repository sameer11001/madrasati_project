package com.webapp.madrasati.auth.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.webapp.madrasati.auth.service.LoginService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private LoginService loginService;

    private JwtTokenUtils jwtTokenUtils;

    private HandlerExceptionResolver handlerExceptionResolver;

    public JwtAuthFilter(LoginService loginService, JwtTokenUtils jwtTokenUtils,
            HandlerExceptionResolver handlerExceptionResolver) {
        this.loginService = loginService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Extract JWT token from the header request
            final String extractToken = extractToken(request);

            // If no token is present, continue with the filter chain
            if (extractToken.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }

            // Extract identifier from the token
            final String username = jwtTokenUtils.getUsernameFromToken(extractToken);

            // Get current authentication status authentication and if username is present
            // and no authentication exists
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                authenticateUser(username, extractToken, request);

            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {

            // Handle any exceptions that occur during the authentication process
            handlerExceptionResolver.resolveException(request, response, null, e);
        }

    }

    /**
     * Authenticate user
     * 
     * @param username
     * @param token
     * @param request
     */
    private void authenticateUser(String username, String token, HttpServletRequest request) {
        AppUserDetails appUserDetails = loginService.loadUserByUsername(username);
        // Validate the token from the utils class
        if (Boolean.TRUE.equals(jwtTokenUtils.validateToken(token, appUserDetails))) {
            // Create authentication token
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    appUserDetails, null, appUserDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // Set the authentication in SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    /**
     * extract token from header and remove 7 string "Bearer "
     * 
     * @param request
     * @return
     */
    private String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return (token != null && token.startsWith("Bearer ")) ? token.substring(7) : "";
    }
}
