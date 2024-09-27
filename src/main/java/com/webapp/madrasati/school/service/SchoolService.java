package com.webapp.madrasati.school.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Page;

import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.dto.req.SchoolCreateBody;
import com.webapp.madrasati.school.model.dto.res.SchoolPageDto;
import com.webapp.madrasati.school.repository.summary.SchoolSummary;

public interface SchoolService {
    List<School> getALLSchools();

    Page<SchoolSummary> getSchoolHomePage(int page, int size);

    School createSchool(SchoolCreateBody schoolCreateBody);

    CompletableFuture<String> insertAll(List<School> school);

    SchoolPageDto getSchoolById(String id);
}
