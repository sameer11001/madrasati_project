package com.webapp.madrasati.school.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.webapp.madrasati.core.repository.BaseRepository;
import com.webapp.madrasati.school.model.SchoolImage;

@Repository
public interface SchoolImageRepository extends BaseRepository<SchoolImage, UUID> {
    List<SchoolImage> findAllBySchoolId(UUID schoolId);
}
