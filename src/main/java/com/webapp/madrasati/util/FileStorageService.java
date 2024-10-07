package com.webapp.madrasati.util;

import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public interface FileStorageService {
    String storeFile(String folderName, UUID objectId, String fileType,MultipartFile file);

    List<String> storeFiles(String folderName, UUID objectId, String fileType,MultipartFile[] files);

    InputStream getFile(String folderName, UUID objectId ,String fileType, String fileName);

    void deleteFile(String folderName, UUID objectId , String fileType, String fileName);

    String getFileUrl(String folderName, UUID objectId, String fileType, String fileName);

}
