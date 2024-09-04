package com.webapp.madrasati.school.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.error.AlreadyExistException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.core.model.ApiResponse;
import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.dto.req.SchoolCreateBody;
import com.webapp.madrasati.school.repository.SchoolRepository;
import com.webapp.madrasati.school.repository.summary.SchoolSummary;

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
        Page<SchoolSummary> schools = schoolRepository.findSchoolSummary(pageable);
        if (schools.isEmpty()) {
            return ApiResponse.success(schools, "No school found", HttpStatus.NO_CONTENT);
        }
        return ApiResponse.success(schools);
    }

    @Transactional
    public ApiResponse<School> createSchool(SchoolCreateBody schoolCreateBody) {
        try {
            if (Boolean.TRUE.equals(schoolRepository.existsBySchoolName(schoolCreateBody.getSchoolName()))) {
                LoggerApp.error("School with name " + schoolCreateBody.getSchoolName() + " already exists.");
                throw new AlreadyExistException(
                        "School with name " + schoolCreateBody.getSchoolName() + " already exists.");
            }

            School school = School.builder()
                    .schoolName(schoolCreateBody.getSchoolName())
                    .schoolCoverImage(schoolCreateBody.getSchoolCoverImage())
                    .schoolEmail(schoolCreateBody.getSchoolEmail())
                    .schoolPhoneNumber(schoolCreateBody.getSchoolPhoneNumber())
                    .schoolAddress(schoolCreateBody.getSchoolAddress())
                    .schoolLocation(schoolCreateBody.getSchoolLocation())
                    .schoolStudentCount(schoolCreateBody.getSchoolStudentCount())
                    .schoolType(schoolCreateBody.getSchoolType())
                    .schoolFound(schoolCreateBody.getSchoolFound())
                    .schoolDescription(schoolCreateBody.getSchoolDescription())
                    .build();

            schoolRepository.save(school);
            return ApiResponse.success(null, "School created successfully", HttpStatus.CREATED);
        } catch (InternalServerError e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ApiResponse<String> insertAll(List<School> school) {
        schoolRepository.saveAllAndFlush(school);
        return ApiResponse.success("All schools created", "successfully", HttpStatus.CREATED);
    }

    public ApiResponse<School> getSchoolById(UUID id) {
        Optional<School> school = schoolRepository.findById(id);
        if (school.isEmpty()) {
            throw new ResourceNotFoundException("This school does not exist.");
        }

        return ApiResponse.success(school.get(), "School found", HttpStatus.OK);
    }
}
