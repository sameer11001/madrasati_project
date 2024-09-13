package com.webapp.madrasati.school_group.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.webapp.madrasati.core.error.AlreadyExistException;
import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.core.model.ApiResponseBody;
import com.webapp.madrasati.school_group.model.Group;
import com.webapp.madrasati.school_group.repository.GroupRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GroupService {

    private GroupRepository groupRepository;

    public ApiResponseBody<Group> createGroup(UUID schoolId) {
        try {
            if (findBySchoolId(schoolId).isPresent()) {
                throw new AlreadyExistException("A group with the given school ID already exists.");
            }
            Group group = Group.builder().schoolId(schoolId).build();
            return ApiResponseBody.success(groupRepository.save(group), "Created Successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public Optional<Group> findBySchoolId(UUID schoolId) {
        return groupRepository.findBySchoolId(schoolId);
    }
}
