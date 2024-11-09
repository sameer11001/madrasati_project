package com.webapp.madrasati.school.service.imp;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.webapp.madrasati.school.model.dto.res.CreateNewSchoolDto;
import com.webapp.madrasati.school.repository.SchoolFeedBackRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.webapp.madrasati.core.error.BadRequestException;
import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.dto.req.SchoolCreateBody;
import com.webapp.madrasati.school.model.dto.res.SchoolProfilePageDto;
import com.webapp.madrasati.school.repository.SchoolRepository;
import com.webapp.madrasati.school.repository.summary.SchoolSummary;
import com.webapp.madrasati.school.service.SchoolService;
import com.webapp.madrasati.util.AppUtilConverter;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SchoolServicesImp implements SchoolService {

    private final SchoolRepository schoolRepository;

    private final SchoolCreateService schoolCreateService;

    private final SchoolProfilePageService schoolProfilePageService;

    private static final AppUtilConverter dataConverter = AppUtilConverter.Instance;

    @Transactional(readOnly = true)
    public CompletableFuture<List<School>> getALLSchools() {
        List<School> schools = schoolRepository.findAll();

        return CompletableFuture.completedFuture(schools);
    }

    @Transactional(readOnly = true)
    public Page<SchoolSummary> getSchoolHomePage(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new BadRequestException("Page number and size must be positive integers.");
        }
        Pageable pageable = PageRequest.of(page, size);
        return schoolRepository.findSchoolSummary(pageable);
    }

    public CreateNewSchoolDto createSchool(SchoolCreateBody schoolCreateBody) {
        return schoolCreateService.createSchool(schoolCreateBody);
    }

    @Transactional
    public List<School> insertAll(List<School> school) {
        try {
            return schoolRepository.saveAll(school);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error while inserting schools: " + e.getMessage());
        }
    }

    public SchoolProfilePageDto fetchSchoolById(String schooIdString) {
        return schoolProfilePageService.getSchoolById(schooIdString);
    }

    public boolean existsById(String schoolIdString) {
        UUID schoolId = dataConverter.stringToUUID(schoolIdString);
        return schoolRepository.existsById(schoolId);
    }

    public School findByIdString(String schoolIdString) {
        UUID schoolId = dataConverter.stringToUUID(schoolIdString);
        return schoolRepository.findById(schoolId).orElseThrow(() -> new ResourceNotFoundException("School not found"));
    }

    public School findById(UUID schoolIdString) {
        return schoolRepository.findById(schoolIdString).orElseThrow(() -> new ResourceNotFoundException("School not found"));
    }

    public Optional<School> findByIdOptional(String schoolIdString) {
        UUID schoolId = dataConverter.stringToUUID(schoolIdString);
        return schoolRepository.findById(schoolId);
    }
}
