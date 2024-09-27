package com.webapp.madrasati.school.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.multipart.MultipartFile;

public interface SchoolImageService {

    CompletableFuture<String> uploadCoverImage(MultipartFile file, UUID schoolId);

    CompletableFuture<String> uploadSchoolImages(MultipartFile[] files, UUID schoolId);
}
