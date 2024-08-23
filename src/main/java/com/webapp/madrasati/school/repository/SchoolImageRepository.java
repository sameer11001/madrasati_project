package com.webapp.madrasati.school.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.webapp.madrasati.core.repository.BaseRepository;
import com.webapp.madrasati.school.model.SchoolImage;

@Repository
public interface SchoolImageRepository extends BaseRepository<SchoolImage, UUID> {

}
