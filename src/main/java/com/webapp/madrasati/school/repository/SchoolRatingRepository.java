package com.webapp.madrasati.school.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.webapp.madrasati.core.repository.BaseRepository;
import com.webapp.madrasati.school.model.SchoolRating;

@Repository
public interface SchoolRatingRepository extends BaseRepository<SchoolRating, UUID> {

}
