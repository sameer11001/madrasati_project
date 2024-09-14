package com.webapp.madrasati.auth.security;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.error.InternalServerErrorException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtils {
    private final Long ACCESS_TOKEN_VALIDITY;

    private final RsaKeyLoader rsaKeyLoader;

    public JwtTokenUtils(
            @Value("${jwt.access.time}") Long accessValidity, RsaKeyLoader rsaKeyLoader) {
        Assert.notNull(accessValidity, "Validity must not be null");
        ACCESS_TOKEN_VALIDITY = accessValidity;
        this.rsaKeyLoader = rsaKeyLoader;
    }

    /**
     * when genrate token send username and determine claim as map <String, T>
     * send to create token method to build JWTS first of all set type of claims
     * set the subject as username and set issuedAt as current time and expiration
     * sign with the my secret key which store application yml
     * 
     * you can set claim as <String, Object>
     * 
     */
    public String generateToken(String username, UUID id) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", id);
        return createToken(claims, username);
    }

    // create token for user
    // subject is username
    private String createToken(Map<String, Object> claims, String username) {
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                    .signWith(
                            getPrivateKey(), SignatureAlgorithm.RS256)
                    .compact();
        } catch (Exception e) {
            LoggerApp.error("Error generating token: " + e.getMessage());
            throw new InternalServerErrorException("Error generating token: " + e.getMessage());
        }

    }

    // retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // retrieve a specific claim from the token
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getPublicKey()).build().parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {

        final String username = getUsernameFromToken(token);
        return (username.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private PrivateKey getPrivateKey() {
        return rsaKeyLoader.getPrivateKey();
    }

    private PublicKey getPublicKey() {
        return rsaKeyLoader.getPublicKey();
    }
}
