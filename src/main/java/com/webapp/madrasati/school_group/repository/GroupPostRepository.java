package com.webapp.madrasati.school_group.repository;

import java.util.List;

import org.bson.types.ObjectId;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.webapp.madrasati.core.repository.MongoBaseRepository;
import com.webapp.madrasati.school_group.model.GroupPost;

@Repository
public interface GroupPostRepository extends MongoBaseRepository<GroupPost, ObjectId> {

    // Fetch paginated GroupPosts by a list of ObjectId references
    @Query("{ '_id': { $in: ?0 } }")
    Page<GroupPost> findByPostIds(List<ObjectId> groupPostIds, Pageable pageable);
}
