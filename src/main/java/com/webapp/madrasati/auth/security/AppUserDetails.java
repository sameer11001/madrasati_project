package com.webapp.madrasati.auth.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.transaction.annotation.Transactional;

import com.webapp.madrasati.auth.model.UserEntity;

@Transactional
public class AppUserDetails implements UserDetails {

    private UUID userId;
    private String userEmail;
    private String userPassword;
    private List<GrantedAuthority> authorities;

    public AppUserDetails() {
        super();
    }

    public AppUserDetails(UserEntity account) {

        this.userId = account.getId();
        this.userEmail = account.getUserEmail();
        this.userPassword = account.getUserPassword();

        this.authorities = new ArrayList<>();
        if (account.getUserRole() != null) {
            authorities.add(new SimpleGrantedAuthority(account.getUserRole().getRoleName()));
            account.getUserRole().getPermissions().forEach(
                    permission -> this.authorities.add(new SimpleGrantedAuthority(permission.getPermissionName())));
        }

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return this.authorities;
    }

    @Override
    public String getPassword() {

        return this.userPassword;
    }

    @Override
    public String getUsername() {

        return this.userEmail;
    }

    public UUID getUserId() {
        return this.userId;
    }

}
