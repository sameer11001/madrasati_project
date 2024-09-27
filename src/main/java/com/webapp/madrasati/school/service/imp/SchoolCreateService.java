package com.webapp.madrasati.school.service.imp;

import org.springframework.stereotype.Service;
import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.error.AlreadyExistException;
import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.dto.req.SchoolCreateBody;
import com.webapp.madrasati.school.repository.SchoolRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SchoolCreateService {

    private final SchoolRepository schoolRepository;

    public School createSchool(SchoolCreateBody schoolCreateBody) {
        try {
            if (Boolean.TRUE.equals(schoolRepository.existsBySchoolName(schoolCreateBody.getSchoolName()))) {
                LoggerApp.error("School with name " + schoolCreateBody.getSchoolName() + " already exists.");
                throw new AlreadyExistException(
                        "School with name " + schoolCreateBody.getSchoolName() + " already exists.");
            }

            School school = School.builder()
                    .schoolName(schoolCreateBody.getSchoolName())
                    .schoolEmail(schoolCreateBody.getSchoolEmail())
                    .schoolPhoneNumber(schoolCreateBody.getSchoolPhoneNumber())
                    .schoolAddress(schoolCreateBody.getSchoolAddress())
                    .schoolLocation(schoolCreateBody.getSchoolLocation())
                    .schoolStudentCount(schoolCreateBody.getSchoolStudentCount())
                    .schoolType(schoolCreateBody.getSchoolType())
                    .schoolFound(schoolCreateBody.getSchoolFound())
                    .schoolDescription(schoolCreateBody.getSchoolDescription())
                    .build();

            return schoolRepository.save(school);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
