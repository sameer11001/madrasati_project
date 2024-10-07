package com.webapp.madrasati.school.service.imp;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    SchoolImageServicesImp(SchoolImageRepository schoolImageRepository, SchoolRepository schoolRepository,FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
        this.schoolImageRepository = schoolImageRepository;
        this.schoolRepository = schoolRepository;
    }

    @CacheEvict(value = "schoolPage", key = "#schoolIdString")
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<String> uploadCoverImage(MultipartFile file, String schoolIdString) {
        UUID schoolId = UUID.fromString(schoolIdString);

        if (!schoolRepository.existsById(schoolId)) {
            throw new ResourceNotFoundException("School not found");
        }

        String fileName = fileStorageService.storeFile( "school", schoolId, "cover_images",file);
        String fileUrl = fileStorageService.getFileUrl("school", schoolId, "cover_images",fileName);

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
    public CompletableFuture<List<String>> uploadSchoolImages(MultipartFile[] files, String schoolIdString) {
        UUID schoolId = UUID.fromString(schoolIdString);

        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new ResourceNotFoundException("School not found"));

        List<String> fileNames = fileStorageService.storeFiles("school", schoolId,"school_images", files);

        Stream<SchoolImage> schoolImageStream = fileNames.stream().map(fileName -> {
            String fileUrl = fileStorageService.getFileUrl("school", schoolId , "school_images",fileName);
            return SchoolImage.builder()
                    .school(school)
                    .imageName(fileName)
                    .imagePath(fileUrl)
                    .build();
        });

        List<SchoolImage> schoolImages = schoolImageStream.collect(Collectors.toList());

        try {
            schoolImageRepository.saveAllAndFlush(schoolImages);
        }catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        return CompletableFuture.completedFuture(fileNames);
    }
}
