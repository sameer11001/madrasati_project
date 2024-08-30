package com.webapp.madrasati.auth.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.webapp.madrasati.auth.service.UserDetailsServiceImp;
import com.webapp.madrasati.core.config.LoggerApp;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired

    private UserDetailsServiceImp userDetailsService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Extract JWT token from the header request
            final String extractToken = extractToken(request);
            LoggerApp.debug("Extracted token: ", extractToken);
            // If no token is present, continue with the filter chain
            if (extractToken.isBlank()) {
                LoggerApp.debug("No token found");
                filterChain.doFilter(request, response);
                return;
            }

            // Extract identifier from the token
            final String username = jwtTokenUtils.getUsernameFromToken(extractToken);
            LoggerApp.debug("Jwt contains Username: {}", username);
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
        AppUserDetails appUserDetails = userDetailsService.loadUserByUsername(username);
        // Validate the token from the utils class
        if (Boolean.TRUE.equals(jwtTokenUtils.validateToken(token, appUserDetails))) {
            // Create authentication token
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    appUserDetails, null, appUserDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // Set the authentication in SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            LoggerApp.debug("Token is not valid for user: { }", username);
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
