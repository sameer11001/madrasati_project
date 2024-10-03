package com.webapp.madrasati.school.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.web.multipart.MultipartFile;

public interface SchoolImageService {

    CompletableFuture<String> uploadCoverImage(MultipartFile file, String schoolIdString);

    CompletableFuture<String> uploadSchoolImages(MultipartFile[] files, String schoolIdString);
}
