package com.webapp.madrasati.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.webapp.madrasati.auth.security.AppUserDetails;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public AppUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return new AppUserDetails(userService.findByUserEmail(email).get());
    }
}
