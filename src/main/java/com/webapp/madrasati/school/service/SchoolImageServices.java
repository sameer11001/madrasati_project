package com.webapp.madrasati.school.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.error.BadRequestException;
import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.core.model.ApiResponse;
import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.SchoolImage;
import com.webapp.madrasati.school.repository.SchoolImageRepository;
import com.webapp.madrasati.school.repository.SchoolRepository;

import jakarta.transaction.Transactional;

@Service
@Lazy
public class SchoolImageServices {
    private String location = "src/main/resources/static";
    private SchoolImageRepository schoolImageRepository;
    private SchoolRepository schoolRepository;

    public SchoolImageServices(SchoolImageRepository schoolImageRepository, SchoolRepository schoolRepository) {
        this.schoolImageRepository = schoolImageRepository;
        this.schoolRepository = schoolRepository;
    }

    @Transactional
    public ApiResponse<String> uploadCoverImage(MultipartFile file, UUID schoolId) {
        try {
            if (file.isEmpty()) {
                throw new BadRequestException("File is empty");
            }

            final String uploadDir = location + "images/school/cover_image";
            Path directory = Paths.get(uploadDir);

            Files.createDirectories(directory);

            Path filePath = directory.resolve(schoolId.toString());
            LoggerApp.info("File path: {}", filePath);

            Files.write(filePath, file.getBytes());

            String relativePath = "images/school/cover_image/" + schoolId;
            schoolRepository.updateSchoolCoverImage(relativePath, schoolId);

            return ApiResponse.success(null, "Image uploaded successfully", HttpStatus.CREATED);
        } catch (IOException e) {
            LoggerApp.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public ApiResponse<String> uploadSchoolImages(MultipartFile[] files, UUID schoolId) {
        try {
            if (files.length == 0) {
                throw new BadRequestException("File is empty");
            }
            final String uploadDir = location + "images/school/school_images/" + schoolId;
            Path directory = Paths.get(uploadDir);

            Files.createDirectories(directory);

            School school = schoolRepository.findById(schoolId)
                    .orElseThrow(() -> new BadRequestException("School not found"));

            List<SchoolImage> schoolImages = Arrays.stream(files)
                    .map(file -> SchoolImage.builder()
                            .imageName(file.getOriginalFilename())
                            .imagePath(uploadDir)
                            .school(school)
                            .build())
                    .collect(Collectors.toList());
            List<SchoolImage> savedImages = schoolImageRepository.saveAllAndFlush(schoolImages);

            for (int i = 0; i < files.length; i++) {
                Path filePath = directory.resolve(savedImages.get(i).getId().toString());
                Files.write(filePath, files[i].getBytes());
            }

            return ApiResponse.success(null, "Image uploaded successfully", HttpStatus.CREATED);
        } catch (IOException e) {
            LoggerApp.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
