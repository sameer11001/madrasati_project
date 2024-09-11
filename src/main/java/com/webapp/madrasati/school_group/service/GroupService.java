package com.webapp.madrasati.school_group.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.webapp.madrasati.core.model.ApiResponseBody;
import com.webapp.madrasati.school_group.model.Group;
import com.webapp.madrasati.school_group.repository.GroupRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GroupService {

    private GroupRepository groupRepository;

    public ApiResponseBody<Group> createGroup(UUID schoolId) {
        Group group = Group.builder().schoolId(schoolId).build();
        return ApiResponseBody.success(groupRepository.save(group), "Created Successfully", HttpStatus.CREATED);
    }

    public Group findBySchoolId(UUID schoolId) {
        return groupRepository.findBySchoolId(schoolId);
    }
}
