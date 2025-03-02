package com.webapp.madrasati.school.repository;

import java.util.List;
import java.util.UUID;


import com.webapp.madrasati.school.model.dto.res.SchoolFeedBackDto;
import com.webapp.madrasati.school.repository.summary.SchoolFeedBackSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.webapp.madrasati.core.repository.BaseRepository;
import com.webapp.madrasati.school.model.SchoolFeedBack;

@Repository
public interface SchoolFeedBackRepository extends BaseRepository<SchoolFeedBack, UUID> {

    List<SchoolFeedBack> findAllBySchoolId(UUID schoolId);

    @Query("SELECT f.id as feedbackId, f.feedbackDescription as feedbackDescription, " +
            "u.id as userId, u.userFirstName as userFirstName, f.createdAt as createdAt " +
            "FROM SchoolFeedBack f " +
            "JOIN f.user u " +
            "WHERE f.school.id = :schoolId")
    Page<SchoolFeedBackSummary> findAllBySchoolIdPageable(UUID schoolId, Pageable pageable);

    @Query("SELECT new com.webapp.madrasati.school.model.dto.res.SchoolFeedBackDto(" +
            "sfb.id, " +
            "sfb.feedbackDescription, " +
            "sfb.user.id, " +
            "sfb.user.userFirstName, " +
            "sfb.createdAt) " +
            "FROM SchoolFeedBack sfb " +
            "WHERE sfb.school.id = :schoolId " +
            "ORDER BY sfb.createdAt DESC")
    List<SchoolFeedBackDto> findBySchoolIdOrderByCreatedAtDesc(UUID schoolId);
}
