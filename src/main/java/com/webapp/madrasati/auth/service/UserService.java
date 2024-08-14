package com.webapp.madrasati.auth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.repository.UserRepository;
import com.webapp.madrasati.core.error.ResourceNotFoundException;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public boolean existsByUserEmail(String email) {
        if (!userRepository.existsByUserEmail(email)) {
            throw new ResourceNotFoundException("Account with email " + email + " does not exist.");
        }
        return true;
    }

    public Optional<UserEntity> findByUserEmail(String email) {
        if (!userRepository.findByUserEmail(email).isPresent()) {
            throw new ResourceNotFoundException("Account with email " + email + " does not exist.");
        }
        return userRepository.findByUserEmail(email);

    }
}
