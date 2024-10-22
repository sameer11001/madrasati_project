package com.webapp.madrasati.util;

import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.List;

public interface FileStorageService {
    String storeFile(String className, String classId, String category, MultipartFile file);

    List<String> storeFiles(String className, String classId, String category, List<MultipartFile> files);

    InputStream getFile(String className, String classId, String category, String fileName);

    boolean deleteFile(String className, String classId, String category, String fileName);

    String getFileUrl(String className, String classId, String category, String fileName);

}
