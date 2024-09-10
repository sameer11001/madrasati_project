// package com.webapp.madrasati.school_group.service;

// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;

// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

// import com.webapp.madrasati.auth.security.UserIdSecurity;
// import com.webapp.madrasati.core.error.BadRequestException;
// import com.webapp.madrasati.core.model.ApiResponse;

// import com.webapp.madrasati.school_group.model.dto.req.GroupPostDto;
// import com.webapp.madrasati.school_group.repository.GroupPostRepository;

// import jakarta.transaction.Transactional;
// import lombok.AllArgsConstructor;

// @Service
// @AllArgsConstructor
// public class GroupPostServices {
// private GroupPostRepository groupPostRepository;
// private UserIdSecurity userIdSecurity;

// @Transactional
// public ApiResponse<String> createPost(MultipartFile[] files, GroupPostDto
// groupPostDto) {

// try {
// if (files.length == 0) {
// throw new BadRequestException("File is empty");
// }
// final String location = "src/main/resources/static";
// final String uploadDir = location + "images/group_post";
// Path directory = Paths.get(uploadDir);
// return null;
// } catch (IOException e) {
// return null;
// }
// }

// }
