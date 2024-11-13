package com.webapp.madrasati.school_group.service;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.webapp.madrasati.core.error.AlreadyExistException;
import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.school_group.model.Group;
import com.webapp.madrasati.school_group.repository.GroupRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    @Transactional
    public Group createGroup(String schoolIdString,String schoolImage) {
        UUID schoolId = UUID.fromString(schoolIdString);
        try {
            if (findBySchoolId(schoolId).isPresent()) {
                throw new AlreadyExistException("A group with the given school ID already exists.");
            }
            Group group = Group.builder().schoolId(schoolId).schoolImagePath(schoolImage).build();
            return groupRepository.save(group);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public boolean insertAll(List<Group> groups) {
        try {
            List<UUID> schoolIds = groups.stream().map(Group::getSchoolId).toList();
            List<Group> existingGroups = groupRepository.findBySchoolIdIn(schoolIds);
            if (!existingGroups.isEmpty()) {
                throw new ResolutionException("One or more groups have an existing schoolId.");
            }

            groupRepository.saveAll(groups);
            return true;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }

    }

    public Optional<Group> findBySchoolId(UUID schoolId) {
        return groupRepository.findBySchoolId(schoolId);
    }
}
