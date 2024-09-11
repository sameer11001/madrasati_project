package com.webapp.madrasati.auth.security;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserIdSecurity {
    public UUID getUId() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof AppUserDetails) {

            AppUserDetails userDetails = (AppUserDetails) principal;
            return userDetails.getUserId();
        } else {

            throw new IllegalStateException("User is not authenticated or userId not available");
        }
    }
}
