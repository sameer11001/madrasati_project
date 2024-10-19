package com.webapp.madrasati.util;

import com.webapp.madrasati.school_group.util.annotation.LoggMethod;
import com.webapp.madrasati.core.config.LoggerApp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.webapp.madrasati.core.error.BadRequestException;
import com.webapp.madrasati.core.error.InternalServerErrorException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path rootLocation;

    private final List<String> allowedFileTypes;

    public LocalFileStorageService(@Value("${file.upload-dir}") String uploadDir,
            @Value("${file.allowed-types}") List<String> allowedFileTypes) {
        this.allowedFileTypes = allowedFileTypes;
        this.rootLocation = Paths.get(uploadDir);
    }

    @LoggMethod
    @Override
    public String storeFile(String className, String classId, String category, MultipartFile file) {
        validateFile(file);
        String fileName = generateFileName(file);
        Path targetLocation = getTargetLocation(className, classId, category, fileName);
        try {
            Files.createDirectories(targetLocation.getParent());

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new InternalServerErrorException("Could not store file " + fileName, e);
        }
    }

    @Override
    public List<String> storeFiles(String className, String classId, String category, MultipartFile[] files) {
       return Arrays.stream(files)
                .map(file -> storeFile(className, classId, category, file))
               .toList();

    }

    @Override
    public InputStream getFile(String className, String classId, String category, String fileName) {
        Path filePath = getTargetLocation(className, classId, category, fileName);
        try {
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            throw new InternalServerErrorException("Could not read file " + fileName, e);
        }
    }

    @LoggMethod
    @Override
    public boolean deleteFile(String className, String classId, String category, String fileName) {
        Path filePath = getTargetLocation(className, classId, category, fileName);
        try {
           return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            LoggerApp.error("Error deleting file: {}", filePath, e);
            throw new InternalServerErrorException("Could not delete file " + fileName, e);
        }
    }

    @Override
    public String getFileUrl(String className, String classId, String category, String fileName) {
        return getTargetLocation(className, classId, category, fileName).toString();
    }

    private Path getTargetLocation(String className, String classId, String category, String fileName) {
        return this.rootLocation.resolve(Paths.get(className, classId, category, fileName));
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Failed to store empty file");
        }
        if (!allowedFileTypes.contains(file.getContentType())) {
            throw new BadRequestException("File type not allowed");
        }
    }

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID() + "_" + file.getOriginalFilename();
    }
}