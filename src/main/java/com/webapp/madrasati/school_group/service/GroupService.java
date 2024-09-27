package com.webapp.madrasati.school_group.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.webapp.madrasati.core.error.AlreadyExistException;
import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.school_group.model.Group;
import com.webapp.madrasati.school_group.repository.GroupRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    public Group createGroup(String schoolIdString) {
        UUID schoolId = UUID.fromString(schoolIdString);
        try {
            if (findBySchoolId(schoolId).isPresent()) {
                throw new AlreadyExistException("A group with the given school ID already exists.");
            }
            Group group = Group.builder().schoolId(schoolId).build();
            return groupRepository.save(group);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public Optional<Group> findBySchoolId(UUID schoolId) {
        return groupRepository.findBySchoolId(schoolId);
    }
}
