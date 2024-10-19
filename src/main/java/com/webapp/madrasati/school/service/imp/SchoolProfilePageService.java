package com.webapp.madrasati.school.service.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.SchoolFeedBack;
import com.webapp.madrasati.school.model.SchoolImage;
import com.webapp.madrasati.school.model.dto.res.SchoolPageDto;
import com.webapp.madrasati.school.repository.SchoolFeedBackRepository;
import com.webapp.madrasati.school.repository.SchoolImageRepository;
import com.webapp.madrasati.school.repository.SchoolRepository;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SchoolProfilePageService {
    private final SchoolRepository schoolRepository;
    private final SchoolImageRepository imageRepository;
    private final SchoolFeedBackRepository schoolFeedBackRepository;

    @Cacheable(value = "schoolPage", key = "#schooIdString")
    @Transactional(readOnly = true)
    public SchoolPageDto getSchoolById(String schooIdString) {
        UUID schoolId = UUID.fromString(schooIdString);
        Optional<School> schoolOpt = schoolRepository.findById(schoolId);
        if (schoolOpt.isEmpty()) {
            LoggerApp.error("School with ID {} not found", schoolId);
            throw new ResourceNotFoundException("School not found");
        }
        School school = schoolOpt.get();
        return mapToDto(school, schoolId);
    }

    private SchoolPageDto mapToDto(School school, UUID schoolId) {
        return SchoolPageDto.builder()
                .schoolId(schoolId.toString())
                .schoolDescription(school.getSchoolDescription())
                .schoolEmail(school.getSchoolEmail())
                .schoolCoverImage(school.getSchoolCoverImage())
                .schoolStudentCount(school.getSchoolStudentCount())
                .schoolFeedBacks(getSchoolFeedBack(schoolId))
                .schoolPhoneNumber(school.getSchoolPhoneNumber())
                .schoolName(school.getSchoolName())
                .schoolLocation(school.getSchoolLocation())
                .averageRating(school.getAverageRating())
                .schoolImages(getSchoolImages(schoolId))
                .teachers(school.getTeachers())
                .build();
    }

    private List<String> getSchoolImages(UUID schoolId) {
        List<SchoolImage> images = imageRepository.findAllBySchoolId(schoolId);
        List<String> imageList = new ArrayList<>();
        if (images != null) {
            images.forEach(image -> imageList.add(image.getImagePath() + '/' + image.getId()));
        }
        return imageList;
    }

    private List<String> getSchoolFeedBack(UUID schoolId) {
        List<SchoolFeedBack> schoolFeedBacks = schoolFeedBackRepository.findAllBySchoolId(schoolId);
        List<String> feedBackList = new ArrayList<>();
        if (schoolFeedBacks != null) {
            schoolFeedBacks.forEach(schoolFeedBack -> feedBackList.add(schoolFeedBack.getFeedbackDescription()));
        }
        return feedBackList;
    }
}
