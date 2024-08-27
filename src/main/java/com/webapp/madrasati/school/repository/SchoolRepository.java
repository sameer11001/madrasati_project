package com.webapp.madrasati.school.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.webapp.madrasati.core.repository.BaseRepository;
import com.webapp.madrasati.school.model.School;

@Repository
public interface SchoolRepository extends BaseRepository<School, UUID> {

    Page<School> findAll(Pageable pageable);
}
