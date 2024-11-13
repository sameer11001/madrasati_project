package com.webapp.madrasati.school.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.webapp.madrasati.school.model.dto.req.SchoolEditBodyDto;
import com.webapp.madrasati.school.model.dto.res.CreateNewSchoolDto;
import com.webapp.madrasati.school.model.dto.res.SchoolEditResponseDto;
import org.springframework.data.domain.Page;

import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.dto.req.SchoolCreateBody;
import com.webapp.madrasati.school.model.dto.res.SchoolProfilePageDto;
import com.webapp.madrasati.school.repository.summary.SchoolSummary;

public interface SchoolService {
    CompletableFuture<List<School>> getALLSchools();

    Page<SchoolSummary> getSchoolHomePage(int page, int size);

    CreateNewSchoolDto createSchool(SchoolCreateBody schoolCreateBody);

    List<School> insertAll(List<School> school);

    SchoolProfilePageDto fetchSchoolById(String id);

    School findByIdString(String schoolIdString);

    School findById(UUID schoolIdString);

    Optional<School> findByIdOptional(String schoolIdString);

    SchoolEditResponseDto editSchoolInfo(SchoolEditBodyDto bodyDto, String schoolIdString);

}
