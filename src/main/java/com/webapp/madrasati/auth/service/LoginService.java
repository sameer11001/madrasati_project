package com.webapp.madrasati.auth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.webapp.madrasati.auth.error.UserNotFoundException;
import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.repository.UserRepository;
import com.webapp.madrasati.auth.security.AppUserDetails;
import com.webapp.madrasati.core.config.LoggerApp;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public AppUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> account = userRepository.findByUserEmail(email);
        if (!account.isPresent()) {
            LoggerApp.error(new UserNotFoundException(), "Account with email {} does not exist.", email);
            throw new UserNotFoundException();
        }
        return new AppUserDetails(account.get());
    }

}
