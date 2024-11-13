package com.webapp.madrasati.school.service.imp;

import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school.mapper.SchoolMapper;
import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.dto.req.SchoolEditBodyDto;
import com.webapp.madrasati.school.model.dto.res.SchoolEditResponseDto;
import com.webapp.madrasati.school.repository.SchoolRepository;
import com.webapp.madrasati.util.AppUtilConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class SchoolEditService {
    private final SchoolRepository schoolRepository;
    private final SchoolMapper schoolmapper;

    private static final AppUtilConverter dataConverter = AppUtilConverter.Instance;

    public SchoolEditResponseDto editSchool(SchoolEditBodyDto bodyDto, String schoolIdString) {
        UUID schoolId = dataConverter.stringToUUID(schoolIdString);
        School school = schoolRepository.findById(schoolId).orElseThrow(() -> new ResourceNotFoundException("School not found"));

        School updatedSchool = schoolmapper.updateSchoolEntity(bodyDto, school);

        schoolRepository.save(updatedSchool);

        return schoolmapper.fromSchoolEntityToSchoolEditResponseDto(updatedSchool);
    }
}
