package com.webapp.madrasati.auth.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.repository.UserRepository;
import com.webapp.madrasati.auth.security.AppUserDetails;

import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImp implements UserDetailsService {


    private final UserRepository userRepository;

    UserDetailsServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public AppUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email " + email));
        return new AppUserDetails(user);
    }
}
