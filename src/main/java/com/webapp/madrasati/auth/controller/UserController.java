package com.webapp.madrasati.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.webapp.madrasati.auth.model.dto.res.UserPageDto;
import com.webapp.madrasati.auth.service.UserServices;
import com.webapp.madrasati.core.model.ApiResponseBody;

@Controller
@RequestMapping("user/v1/")
public class UserController {
    private final UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping("getUserPage")
    public ApiResponseBody<UserPageDto> getUserProfilePage() {
        return ApiResponseBody.success(userServices.getUserPageByUserId());
    }

}
