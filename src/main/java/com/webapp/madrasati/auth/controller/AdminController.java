package com.webapp.madrasati.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.madrasati.auth.mapper.UserMapper;
import com.webapp.madrasati.auth.model.dto.UserEntityDto;
import com.webapp.madrasati.auth.service.UserServices;
import com.webapp.madrasati.core.model.ApiResponseBody;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    UserServices userService;

    UserMapper userMapper;

    AdminController(UserServices userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/create/school_manger")
    public ApiResponseBody<UserEntityDto> adminCreateSchoolManger(@RequestBody UserEntityDto entity) {
        UserEntityDto userEntityDto = userMapper.toUserEntityDto(userService.createSchoolManager(entity));
        return ApiResponseBody.success(userEntityDto, "User created successfully", HttpStatus.CREATED);
    }

}
