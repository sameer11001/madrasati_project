package com.webapp.madrasati.auth.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.madrasati.auth.mapper.UserMapper;
import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.model.dto.UserEntityDto;
import com.webapp.madrasati.auth.repository.UserRepository;
import com.webapp.madrasati.auth.util.RoleAppConstant;
import com.webapp.madrasati.core.error.AlreadyExistException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServices {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleServices roleService;
    private final PasswordEncoder passwordEncoder;

    public boolean existsByUserEmail(String email) {
        if (!userRepository.existsByUserEmail(email)) {
            throw new ResourceNotFoundException("Account with email " + email + " does not exist.");
        }
        return true;
    }

    public Optional<UserEntity> findByUserEmail(String email) {
        return userRepository.findByUserEmail(email);
    }

    @Transactional
    public UserEntity createStudent(UserEntityDto userEntityDto) {
        UserEntity userEntity = userMapper.toUserEntity(userEntityDto);
        validateUserEmail(userEntity.getUserEmail());
        userEntity.setUserRole(roleService.findByRoleName(RoleAppConstant.STUDENT.getString())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found")));
        userEntity.setUserPassword(passwordEncoder.encode(userEntity.getUserPassword()));
        return userRepository.save(userEntity);
    }

    public boolean insertAll(List<UserEntityDto> users) {
        users.forEach(this::createStudent);
        return true;
    }

    @Transactional
    public UserEntity createSchoolManager(UserEntityDto userEntityDto) {
        UserEntity userEntity = userMapper.toUserEntity(userEntityDto);
        validateUserEmail(userEntity.getUserEmail());
        userEntity.setUserRole(roleService.findByRoleName(RoleAppConstant.SMANAGER.getString())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found")));
        userEntity.setUserPassword(passwordEncoder.encode(userEntity.getUserPassword()));
        return userRepository.save(userEntity);
    }

    private void validateUserEmail(String email) {
        if (userRepository.existsByUserEmail(email)) {
            throw new AlreadyExistException("Account with email " + email);
        }
    }

}