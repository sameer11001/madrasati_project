package com.webapp.madrasati.auth.security;

import java.io.IOException;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.webapp.madrasati.auth.error.NoTokenFoundException;
import com.webapp.madrasati.auth.error.TokenNotValidException;
import com.webapp.madrasati.auth.error.TooManyRequestException;
import com.webapp.madrasati.auth.repository.RefresherTokenRepostiory;
import com.webapp.madrasati.auth.service.UserDetailsServiceImp;
import com.webapp.madrasati.core.config.LoggerApp;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImp userDetailsService;
    private final JwtTokenUtils jwtTokenUtils;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final RefresherTokenRepostiory refresherTokenRepository;
    private final RateLimiterService rateLimiterService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String extractToken = extractToken(request);
        LoggerApp.debug("Extracted token: {}", extractToken);

        if (extractToken.isBlank()) {
            LoggerApp.debug("No token");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String username = jwtTokenUtils.getUsernameFromToken(extractToken);

            if (!rateLimiterService.isRequestAllowed(extractToken)) {
                throw new TooManyRequestException("Too Many Request");
            }

            LoggerApp.debug("Jwt contains Username: {}", username);
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                authenticateUser(username, extractToken, request);
            }
            filterChain.doFilter(request, response);
        } catch (TokenNotValidException | TooManyRequestException | NoTokenFoundException | ExpiredJwtException |
                 IllegalArgumentException |
                 UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            LoggerApp.error("Error while filtering request: {}", e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    private void authenticateUser(String username, String token, HttpServletRequest request) {
        AppUserDetails appUserDetails = userDetailsService.loadUserByUsername(username);
        boolean isTokenExist = refresherTokenRepository.existsByUserEmail(username);
        if (isTokenExist) {
            if (Boolean.TRUE.equals(jwtTokenUtils.validateToken(token, appUserDetails))) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        appUserDetails, null, appUserDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                LoggerApp.debug("Token is not valid for user: { }", username);
                throw new TokenNotValidException("Token is not valid.");
            }
        } else {
            LoggerApp.debug("Token is not exist for user: { }", username);
            throw new NoTokenFoundException("Token is not exist.");
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
