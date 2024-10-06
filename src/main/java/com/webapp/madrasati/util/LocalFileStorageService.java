package com.webapp.madrasati.util;

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
import java.util.stream.Collectors;

@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path rootLocation;

    private List<String> allowedFileTypes;

    public LocalFileStorageService(@Value("${file.upload-dir}") String uploadDir,
            @Value("${file.allowed-types}") List<String> allowedFileTypes) {
        this.allowedFileTypes = allowedFileTypes;
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.rootLocation);
        } catch (IOException e) {
            throw new InternalServerErrorException("Could not create upload directory", e);
        }
    }

    @Override
    public String storeFile(MultipartFile file, String folderName, String fileType, UUID objectId) {
        validateFile(file);
        String fileName = generateFileName(file);
        Path targetLocation = getTargetLocation(folderName, fileType, objectId, fileName);
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new InternalServerErrorException("Could not store file " + fileName, e);
        }
    }

    @Override
    public List<String> storeFiles(MultipartFile[] files, String folderName, String fileType, UUID objectId) {
        return Arrays.stream(files)
                .map(file -> storeFile(file, folderName, fileType, objectId))
                .collect(Collectors.toList());
    }

    @Override
    public InputStream getFile(String fileName, String folderName, String fileType, UUID objectId) {
        Path filePath = getTargetLocation(folderName, fileType, objectId, fileName);
        try {
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            throw new InternalServerErrorException("Could not read file " + fileName, e);
        }
    }

    @Override
    public void deleteFile(String fileName, String folderName, String fileType, UUID objectId) {
        Path filePath = getTargetLocation(folderName, fileType, objectId, fileName);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new InternalServerErrorException("Could not delete file " + fileName, e);
        }
    }

    @Override
    public String getFileUrl(String fileName, String folderName, String fileType, UUID objectId) {
        return getTargetLocation(folderName, fileType, objectId, fileName).toString();
    }

    private Path getTargetLocation(String folderName, String fileType, UUID objectId, String fileName) {
        return this.rootLocation.resolve(Paths.get(folderName, fileType, objectId.toString(), fileName)).normalize();
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
        return UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    }
}