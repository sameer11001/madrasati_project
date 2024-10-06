package com.webapp.madrasati.school.service.imp;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.SchoolImage;
import com.webapp.madrasati.school.repository.SchoolImageRepository;
import com.webapp.madrasati.school.repository.SchoolRepository;
import com.webapp.madrasati.util.FileStorageService;

@Service
public class SchoolImageUploadService {

    private final SchoolRepository schoolRepository;
    private final SchoolImageRepository schoolImageRepository;
    private final FileStorageService fileStorageService;

    public SchoolImageUploadService(SchoolRepository schoolRepository,
            SchoolImageRepository schoolImageRepository,
            FileStorageService fileStorageService) {
        this.schoolRepository = schoolRepository;
        this.schoolImageRepository = schoolImageRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public String uploadCoverImage(MultipartFile file, String schoolIdString) {
        UUID schoolId = UUID.fromString(schoolIdString);

        if (!schoolRepository.existsById(schoolId)) {
            throw new ResourceNotFoundException("School not found");
        }

        String fileName = fileStorageService.storeFile(file, "school", "cover_images", schoolId);
        String fileUrl = fileStorageService.getFileUrl(fileName, "school", "cover_images", schoolId);

        schoolRepository.updateSchoolCoverImage(fileUrl, schoolId);

        return fileUrl;
    }

    @Transactional
    public List<String> uploadSchoolImages(MultipartFile[] files, String schoolIdString) {
        UUID schoolId = UUID.fromString(schoolIdString);

        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new ResourceNotFoundException("School not found"));

        List<String> fileNames = fileStorageService.storeFiles(files, "school", "school_images", schoolId);

        Stream<SchoolImage> schoolImageStream = fileNames.stream().map(fileName -> {
            String fileUrl = fileStorageService.getFileUrl(fileName, "school", "school_images", schoolId);
            return SchoolImage.builder()
                    .school(school)
                    .imageName(fileName)
                    .imagePath(fileUrl)
                    .build();
        });

        List<SchoolImage> schoolImages = schoolImageStream.collect(Collectors.toList());

        schoolImageRepository.saveAllAndFlush(schoolImages);

        return fileNames;
    }

}