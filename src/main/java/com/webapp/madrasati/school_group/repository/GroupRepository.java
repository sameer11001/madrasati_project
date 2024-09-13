package com.webapp.madrasati.school_group.repository;

import java.util.Optional;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.webapp.madrasati.core.repository.MongoBaseRepository;
import com.webapp.madrasati.school_group.model.Group;

@Repository
public interface GroupRepository extends MongoBaseRepository<Group, ObjectId> {
    Optional<Group> findBySchoolId(UUID schoolId);
}
