package com.webapp.madrasati.util;

import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public interface FileStorageService {
    String storeFile(MultipartFile file, String folderName, String fileType, UUID schoolId);

    List<String> storeFiles(MultipartFile[] files, String folderName, String fileType, UUID schoolId);

    InputStream getFile(String fileName, String folderName, String fileType, UUID schoolId);

    void deleteFile(String fileName, String folderName, String fileType, UUID schoolId);

    String getFileUrl(String fileName, String folderName, String fileType, UUID schoolId);
}
