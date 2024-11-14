package com.webapp.madrasati.school.service.imp;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.webapp.madrasati.util.AppUtilConverter;
import com.webapp.madrasati.util.FileStorageService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.SchoolImage;
import com.webapp.madrasati.school.repository.SchoolImageRepository;
import com.webapp.madrasati.school.repository.SchoolRepository;
import com.webapp.madrasati.school.service.SchoolImageService;

@Service
public class SchoolImageServicesImp implements SchoolImageService {

    private final SchoolImageRepository schoolImageRepository;
    private final SchoolRepository schoolRepository;
    private final FileStorageService fileStorageService;
    private static final String CLASS_FOLDER_NAME = "school";
    private static final String SCHOOL_COVER = "cover-image";
    private static final String SCHOOL_IMAGES = "school-images";

    private static final AppUtilConverter dataConverter = AppUtilConverter.Instance;

    SchoolImageServicesImp(SchoolImageRepository schoolImageRepository, SchoolRepository schoolRepository,
            FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
        this.schoolImageRepository = schoolImageRepository;
        this.schoolRepository = schoolRepository;
    }

    @CacheEvict(value = "schoolPage", key = "#schoolIdString")
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<String> uploadCoverImage(MultipartFile file, String schoolIdString) {
        UUID schoolId = dataConverter.stringToUUID(schoolIdString);

        if (!schoolRepository.existsById(schoolId)) {
            throw new ResourceNotFoundException("School not found");
        }

        String fileName = fileStorageService.storeFile(CLASS_FOLDER_NAME, schoolIdString, SCHOOL_COVER, file);
        String fileUrl = fileStorageService.getTargetLocationTrimed(CLASS_FOLDER_NAME, schoolIdString, SCHOOL_COVER, fileName);

        try {
            schoolRepository.updateSchoolCoverImage(fileUrl, schoolId);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        return CompletableFuture.completedFuture(fileUrl);
    }

    @CacheEvict(value = "schoolPage", key = "#schoolIdString")
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<List<String>> uploadSchoolImages(List<MultipartFile> files, String schoolIdString) {
        UUID schoolId = dataConverter.stringToUUID(schoolIdString);

        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new ResourceNotFoundException("School not found"));
        List<String> fileNames = fileStorageService.storeFiles(CLASS_FOLDER_NAME, schoolIdString, SCHOOL_IMAGES, files);

        Stream<SchoolImage> schoolImageStream = fileNames.stream().map(fileName -> {
            String fileUrl = fileStorageService.getTargetLocationTrimed(CLASS_FOLDER_NAME, schoolIdString, SCHOOL_IMAGES, fileName);
            return SchoolImage.builder()
                    .school(school)
                    .imageName(fileName)
                    .imagePath(fileUrl)
                    .build();
        });
        List<SchoolImage> schoolImages = schoolImageStream.toList();

        try {
            schoolImageRepository.saveAllAndFlush(schoolImages);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        List<String> imagesPath = fileNames.stream()
                .map(fileName -> fileStorageService.getFileUrl(schoolIdString, schoolIdString, schoolIdString,
                        fileName))
                .toList();
        return CompletableFuture.completedFuture(imagesPath);
    }
}
