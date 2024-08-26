package com.webapp.madrasati.school.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.webapp.madrasati.core.model.ApiResponse;
import com.webapp.madrasati.school.model.School;

import com.webapp.madrasati.school.repository.SchoolRepository;
import com.webapp.madrasati.school.repository.SchoolSummary;

@Service
public class SchoolService {
    private SchoolRepository schoolRepository;

    public SchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;

    }

    public List<School> getALLSchools() {
        return schoolRepository.findAll();
    }

    public ApiResponse<Page<SchoolSummary>> getSchoolHomePage(int page, int size) {
        if (page < 0 || size <= 0) {
            return ApiResponse.error("Page number and size must be positive integers.", HttpStatus.BAD_REQUEST);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<SchoolSummary> schools = schoolRepository.findAllSchoolsWithSummary(pageable);
        if (schools.isEmpty()) {
            return ApiResponse.success(schools, "No school found", HttpStatus.NO_CONTENT);
        }
        return ApiResponse.success(schools);
    }
}
