package com.webapp.madrasati.school.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.webapp.madrasati.core.repository.BaseRepository;
import com.webapp.madrasati.school.model.SchoolFeedBack;

@Repository
public interface SchoolFeedBackRepository extends BaseRepository<SchoolFeedBack, UUID> {

    List<SchoolFeedBack> findAllBySchoolId(UUID schoolId);

}
