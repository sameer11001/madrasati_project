package com.webapp.madrasati.school_group.repository;

import com.webapp.madrasati.core.repository.MongoBaseRepository;
import com.webapp.madrasati.school_group.model.ImagePost;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagePostRepository extends MongoBaseRepository<ImagePost, ObjectId> {
}
