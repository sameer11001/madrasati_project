package com.webapp.madrasati.school.service.imp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.error.BadRequestException;
import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.SchoolImage;
import com.webapp.madrasati.school.repository.SchoolImageRepository;
import com.webapp.madrasati.school.repository.SchoolRepository;
import com.webapp.madrasati.school.service.SchoolImageService;

@Service
public class SchoolImageServicesimp implements SchoolImageService {

    private final SchoolImageRepository schoolImageRepository;
    private final SchoolRepository schoolRepository;
    private String location;

    SchoolImageServicesimp(SchoolImageRepository schoolImageRepository, SchoolRepository schoolRepository,
            @Value("${upload_dir}") String location) {
        this.location = location;
        this.schoolImageRepository = schoolImageRepository;
        this.schoolRepository = schoolRepository;
    }

    @Async("taskExecutor")
    @Transactional(rollbackFor = { BadRequestException.class, InternalServerErrorException.class, IOException.class })
    public CompletableFuture<String> uploadCoverImage(MultipartFile file, UUID schoolId) {
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

            return CompletableFuture.completedFuture(relativePath);
        } catch (IOException | NullPointerException e) {
            LoggerApp.error("Error while uploading cover image", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Async("taskExecutor")
    @Transactional(rollbackFor = { BadRequestException.class, InternalServerErrorException.class, IOException.class })
    public CompletableFuture<String> uploadSchoolImages(MultipartFile[] files, UUID schoolId) {
        try {
            if (files.length == 0) {
                throw new BadRequestException("File is empty");
            }
            final String uploadDir = location + "images/school/school_images/" + schoolId;
            Path pathDir = Paths.get(uploadDir);

            Files.createDirectories(pathDir);

            School school = schoolRepository.findById(schoolId)
                    .orElseThrow(() -> new BadRequestException("School not found"));

            List<SchoolImage> schoolImages = Arrays.stream(files)
                    .map(file -> SchoolImage.builder()
                            .imageName(file.getOriginalFilename())
                            .imagePath(uploadDir)
                            .school(school)
                            .build())
                    .toList();
            List<SchoolImage> savedImages = schoolImageRepository.saveAllAndFlush(schoolImages);

            for (int i = 0; i < files.length; i++) {
                Path filePath = pathDir.resolve(savedImages.get(i).getId().toString());
                Files.write(filePath, files[i].getBytes());
            }
            return CompletableFuture.completedFuture("inserted successfully");

        } catch (IOException | NullPointerException e) {
            LoggerApp.error(e.getMessage());
            throw new InternalServerErrorException("Error while uploading school images", e);
        }
    }
}
