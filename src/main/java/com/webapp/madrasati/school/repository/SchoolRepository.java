package com.webapp.madrasati.school.repository;

import java.util.UUID;

import com.webapp.madrasati.school.repository.summary.SchoolSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.webapp.madrasati.core.repository.BaseRepository;
import com.webapp.madrasati.school.model.School;

@Repository
public interface SchoolRepository extends BaseRepository<School, UUID> {

    @Query("SELECT s.Id as id, s.schoolName as schoolName, s.schoolCoverImage as schoolCoverImage, s.schoolType as schoolType, "
            +
            "(SELECT AVG(sr.rating) FROM SchoolRating sr WHERE sr.school = s) as averageRating " +
            "FROM School s")
    Page<SchoolSummary> findSchoolSummary(Pageable pageable);

    Boolean existsBySchoolName(String schoolName);

    @Modifying
    @Query("UPDATE School s SET s.schoolCoverImage = :schoolCoverImage WHERE s.id = :id")
    void updateSchoolCoverImage(@Param("schoolCoverImage") String schoolCoverImage, @Param("id") UUID id);
}
