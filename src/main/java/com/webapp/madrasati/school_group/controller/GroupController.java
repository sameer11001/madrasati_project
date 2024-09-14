package com.webapp.madrasati.school_group.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.madrasati.core.model.ApiResponseBody;
import com.webapp.madrasati.school_group.model.Group;
import com.webapp.madrasati.school_group.service.GroupService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/group")
@AllArgsConstructor
public class GroupController {
    private GroupService groupService;

    @PostMapping("/createGroup")
    public ApiResponseBody<Group> createGroup(@RequestParam("schoolId") UUID schoolId) {
        return groupService.createGroup(schoolId);
    }

}
// TODO : Start to services in group controller