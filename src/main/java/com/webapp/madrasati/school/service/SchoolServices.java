package com.webapp.madrasati.school.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.error.AlreadyExistException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.core.model.ApiResponseBody;
import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.dto.req.SchoolCreateBody;
import com.webapp.madrasati.school.model.dto.res.SchoolPageDto;
import com.webapp.madrasati.school.repository.SchoolRepository;
import com.webapp.madrasati.school.repository.summary.SchoolSummary;

@Service
@Transactional
public class SchoolServices {
    private SchoolRepository schoolRepository;

    public SchoolServices(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    public List<School> getALLSchools() {
        return schoolRepository.findAll();
    }

    public ApiResponseBody<Page<SchoolSummary>> getSchoolHomePage(int page, int size) {
        if (page < 0 || size <= 0) {
            return ApiResponseBody.error("Page number and size must be positive integers.", HttpStatus.BAD_REQUEST);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<SchoolSummary> schools = schoolRepository.findSchoolSummary(pageable);
        if (schools.isEmpty()) {
            return ApiResponseBody.success(schools, "No school found", HttpStatus.NO_CONTENT);
        }
        return ApiResponseBody.success(schools);
    }

    public ApiResponseBody<School> createSchool(SchoolCreateBody schoolCreateBody) {
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
            return ApiResponseBody.success(null, "School created successfully", HttpStatus.CREATED);
        } catch (InternalServerError e) {
            return ApiResponseBody.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ApiResponseBody<String> insertAll(List<School> school) {
        schoolRepository.saveAllAndFlush(school);
        return ApiResponseBody.success("All schools created", "successfully", HttpStatus.CREATED);
    }

    public ApiResponseBody<SchoolPageDto> getSchoolById(UUID id) {
        School school = schoolRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("School not found"));
        SchoolPageDto schoolPageDto = SchoolPageDto.builder()
                .schoolDescription(school.getSchoolDescription())
                .schoolEmail(school.getSchoolEmail())
                .schoolStudentCount(school.getSchoolStudentCount())
                .schoolFeedBacks(school.getSchoolFeedBacks())
                .schoolPhoneNumber(school.getSchoolPhoneNumber())
                .schoolName(school.getSchoolName())
                .schoolLocation(school.getSchoolLocation())
                .averageRating(school.getAverageRating())
                .teachers(school.getTeachers()).build();
        return ApiResponseBody.success(schoolPageDto, "School Retrieved", HttpStatus.OK);
    }
}
