package com.webapp.madrasati.school.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.multipart.MultipartFile;

public interface SchoolImageService {

    CompletableFuture<String> uploadCoverImage(MultipartFile file, String schoolIdString);

    CompletableFuture<List<String>> uploadSchoolImages(MultipartFile[] files, String schoolIdString);
}
