package com.webapp.madrasati.school.service;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public interface SchoolImageService {

    String uploadCoverImage(MultipartFile file, UUID schoolId);

    void uploadSchoolImages(MultipartFile[] files, UUID schoolId);
}
