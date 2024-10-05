package com.webapp.madrasati.auth.service;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.madrasati.auth.mapper.UserMapper;
import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.model.dto.UserEntityDto;
import com.webapp.madrasati.auth.model.dto.res.UserPageDto;
import com.webapp.madrasati.auth.repository.UserRepository;
import com.webapp.madrasati.auth.security.UserIdSecurity;
import com.webapp.madrasati.auth.util.RoleAppConstant;
import com.webapp.madrasati.core.error.AlreadyExistException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class UserServices {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleServices roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserIdSecurity userId;

    @Transactional(readOnly = true)
    public boolean existsByUserEmail(String email) {
        if (!userRepository.existsByUserEmail(email)) {
            throw new ResourceNotFoundException("Account with email " + email + " does not exist.");
        }
        return true;
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> findByUserEmail(String email) {
        return userRepository.findByUserEmail(email);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "userPageCache", key = "#userId.getUId()", unless = "#result == null")
    public UserPageDto getUserPageByUserId() {
        UserEntity user = userRepository.findById(userId.getUId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return UserPageDto.builder()
                .userName(user.getUserFirstName() + " " + user.getUserLastName())
                .password(user.getUserPassword())
                .school(user.getUserSchool().getSchoolName())
                .image(user.getUserImage()).build();
    }

    public UserEntity createUser(UserEntityDto userEntityDto, RoleAppConstant roleAppConstant) {
        UserEntity userEntity = userMapper.toUserEntity(userEntityDto);
        validateUserEmail(userEntity.getUserEmail());
        userEntity.setUserRole(roleService.findByRoleName(
                roleAppConstant.toString())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found")));
        userEntity.setUserPassword(passwordEncoder.encode(userEntity.getUserPassword()));
        return userRepository.save(userEntity);
    }

    public boolean insertAll(List<UserEntityDto> users, RoleAppConstant roleAppConstant) {
        for (UserEntityDto user : users) {
            createUser(user, roleAppConstant);
        }
        return true;
    }

    private void validateUserEmail(String email) {
        if (userRepository.existsByUserEmail(email)) {
            throw new AlreadyExistException("Account with email " + email);
        }
    }

}