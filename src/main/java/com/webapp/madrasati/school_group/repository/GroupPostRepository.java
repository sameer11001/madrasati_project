package com.webapp.madrasati.school_group.repository;

import org.bson.types.ObjectId;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.webapp.madrasati.core.repository.MongoBaseRepository;
import com.webapp.madrasati.school_group.model.GroupPost;

@Repository
public interface GroupPostRepository extends MongoBaseRepository<GroupPost, ObjectId> {

    Page<GroupPost> findByGroupId(ObjectId groupId, Pageable pageable);
}
