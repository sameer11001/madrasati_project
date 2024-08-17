package com.webapp.madrasati.auth.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.webapp.madrasati.auth.mapper.UserMapper;
import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.model.dto.UserEntityDto;
import com.webapp.madrasati.auth.repository.UserRepository;
import com.webapp.madrasati.auth.util.RoleAppConstant;
import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.error.AlreadyExistException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean existsByUserEmail(String email) {
        if (!userRepository.existsByUserEmail(email)) {
            LoggerApp.error(new ResourceNotFoundException(), "Account with email {} does not exist.", email);
            throw new ResourceNotFoundException("Account with email " + email + " does not exist.");
        }
        return true;
    }

    public Optional<UserEntity> findByUserEmail(String email) {
        return userRepository.findByUserEmail(email);
    }

    public UserEntity createStudent(UserEntityDto userEntityDto) {
        UserEntity userEntity = userMapper.toUserEntity(userEntityDto);
        validateUserEmail(userEntity.getUserEmail());
        userEntity.setUserRole(roleService.findByRoleName(RoleAppConstant.STUDENT.getString())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found")));
        userEntity.setUserPassword(passwordEncoder.encode(userEntity.getUserPassword()));
        LoggerApp.info("Created student with email {}", userEntity.getUserEmail());
        return userRepository.save(userEntity);
    }

    public boolean insertAll(List<UserEntityDto> dto) {
        dto.forEach(this::createStudent);
        LoggerApp.info("Inserted {} students", dto.size());
        return true;
    }

    public UserEntity createSchoolManager(UserEntityDto userEntityDto) {
        UserEntity userEntity = userMapper.toUserEntity(userEntityDto);
        validateUserEmail(userEntity.getUserEmail());
        userEntity.setUserRole(roleService.findByRoleName(RoleAppConstant.SMANAGER.getString())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found")));
        userEntity.setUserPassword(passwordEncoder.encode(userEntity.getUserPassword()));
        LoggerApp.info("Created school manager with email {}", userEntity.getUserEmail());
        return userRepository.save(userEntity);
    }

    private void validateUserEmail(String email) {
        if (userRepository.existsByUserEmail(email)) {
            LoggerApp.error("Account with email {} already exists.", email);
            throw new AlreadyExistException("Account with email " + email);
        }
    }
}