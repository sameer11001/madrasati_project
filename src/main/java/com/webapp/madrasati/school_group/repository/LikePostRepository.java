package com.webapp.madrasati.school_group.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.webapp.madrasati.core.repository.MongoBaseRepository;
import com.webapp.madrasati.school_group.model.LikePost;

@Repository
public interface LikePostRepository extends MongoBaseRepository<LikePost, ObjectId> {
    Optional<LikePost> findByPostIdAndUserId(ObjectId postId, UUID userId);
    List<LikePost> findByPostIdIn(List<ObjectId> postIds);
}
