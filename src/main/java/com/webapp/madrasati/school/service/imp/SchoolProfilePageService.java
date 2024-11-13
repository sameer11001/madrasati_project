package com.webapp.madrasati.school.service.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.webapp.madrasati.school.mapper.SchoolMapper;
import com.webapp.madrasati.school.model.dto.res.SchoolFeedBackDto;
import com.webapp.madrasati.util.AppUtilConverter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.SchoolImage;
import com.webapp.madrasati.school.model.dto.res.SchoolProfilePageDto;
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
    private final SchoolMapper mapper;

    private static final AppUtilConverter dataConverter = AppUtilConverter.Instance;

    @Cacheable(value = "schoolPage", key = "#schooIdString")
    @Transactional(readOnly = true)
    public SchoolProfilePageDto getSchoolById(String schooIdString) {
        UUID schoolId = dataConverter.stringToUUID(schooIdString);
        Optional<School> schoolOpt = schoolRepository.findById(schoolId);
        if (schoolOpt.isEmpty()) {
            LoggerApp.error("School with ID {} not found", schoolId);
            throw new ResourceNotFoundException("School not found");
        }
        School school = schoolOpt.get();
        return mapToDto(school, schoolId);
    }

    private SchoolProfilePageDto mapToDto(School school, UUID schoolId) {
        SchoolProfilePageDto schoolProfilePageDto = mapper.fromSchoolEntityToSchoolProfilePageDto(school);
        schoolProfilePageDto.setSchoolImages(getSchoolImages(schoolId));
        schoolProfilePageDto.setSchoolFeedBacks(getSchoolFeedBack(schoolId));
        return schoolProfilePageDto;
    }

    private List<String> getSchoolImages(UUID schoolId) {
        List<SchoolImage> images = imageRepository.findAllBySchoolId(schoolId);
        List<String> imageList = new ArrayList<>();
        if (images != null) {
            images.forEach(image -> imageList.add(image.getImagePath() + '/' + image.getId()));
        }
        return imageList;
    }

    private List<SchoolFeedBackDto> getSchoolFeedBack(UUID schoolId) {
        List<SchoolFeedBackDto> schoolFeedBacks = schoolFeedBackRepository.findBySchoolIdOrderByCreatedAtDesc(schoolId);
        if (schoolFeedBacks.isEmpty()) {
            return new ArrayList<>();
        }
        return schoolFeedBacks;
    }
}
