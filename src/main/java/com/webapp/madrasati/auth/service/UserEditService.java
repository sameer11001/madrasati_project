package com.webapp.madrasati.auth.service;

import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.security.UserIdSecurity;
import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.util.AppUtilConverter;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserEditService {

    private final UserServices userService;
    private final UserIdSecurity userIdSecurity;
    private final PasswordEncoder passwordEncoder;
    private static final AppUtilConverter dataConverter = AppUtilConverter.Instance;

    public void changePassword(String oldPassword, String newPassword) {

        UserEntity userEntity = userService.findByUserId(userIdSecurity.getUId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(oldPassword, userEntity.getUserPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        try {
            userEntity.setUserPassword(passwordEncoder.encode(newPassword));

            userService.saveUserEntity(userEntity);

        } catch (DataAccessException e) {
            throw new InternalServerErrorException("error while editing password " + e.getMessage());
        }

    }
}
