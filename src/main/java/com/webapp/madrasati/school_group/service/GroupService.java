package com.webapp.madrasati.school_group.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.webapp.madrasati.school_group.model.Group;
import com.webapp.madrasati.school_group.repository.GroupRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GroupService {
    private GroupRepository groupRepository;

    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    public Group findBySchoolId(UUID schoolId) {
        return groupRepository.findBySchoolId(schoolId);
    }
}
