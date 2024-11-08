package com.webapp.madrasati.auth.controller;

import com.webapp.madrasati.auth.model.dto.req.UserEditPassword;
import com.webapp.madrasati.auth.service.UserEditService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.webapp.madrasati.auth.model.dto.res.UserPageDto;
import com.webapp.madrasati.auth.service.UserServices;
import com.webapp.madrasati.core.model.ApiResponseBody;


@RequestMapping("v1/user")
@AllArgsConstructor
@RestController
public class UserController {
    private final UserServices userServices;
    private final UserEditService userEditService;

    @GetMapping("getUserPage")
    public ApiResponseBody<UserPageDto> getUserProfilePage() {
        return ApiResponseBody.success(userServices.getUserPageByUserId());
    }

    @PutMapping("changePassword")
    public ApiResponseBody<Void> changePassword(@Valid @RequestBody UserEditPassword body) {
           userEditService.changePassword(body.getOldPassword(), body.getNewPassword());
           return ApiResponseBody.successWithNoData;
    }

}
