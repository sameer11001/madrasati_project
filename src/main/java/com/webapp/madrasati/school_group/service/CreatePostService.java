package com.webapp.madrasati.school_group.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.madrasati.auth.security.UserIdSecurity;
import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.core.model.ApiResponse;
import com.webapp.madrasati.school_group.model.Group;
import com.webapp.madrasati.school_group.model.GroupPost;
import com.webapp.madrasati.school_group.model.ImagePost;
import com.webapp.madrasati.school_group.model.dto.req.GroupPostDto;
import com.webapp.madrasati.school_group.repository.GroupPostRepository;
import com.webapp.madrasati.school_group.repository.GroupRepository;

@Service
public class CreatePostService {
    private GroupPostRepository groupPostRepository;
    private GroupRepository groupRepository;
    private UserIdSecurity userIdSecurity;
    private String location;

    public CreatePostService(GroupPostRepository groupPostRepository, GroupRepository groupRepository,
            UserIdSecurity userIdSecurity, @Value("${upload_dir}") String location) {
        this.location = location;
        this.userIdSecurity = userIdSecurity;
        this.groupPostRepository = groupPostRepository;
        this.groupRepository = groupRepository;
    }

    public ApiResponse<String> createPost(MultipartFile[] files, GroupPostDto groupPostDto, String groupIdString) {
        try {
            List<ImagePost> imagesPost = new ArrayList<>();
            ObjectId groupId = new ObjectId(groupIdString);
            if (files.length == 0) {
                final String uploadDir = location + "images/groups/" + groupId + "/posts";
                Path directory = Paths.get(uploadDir);

                Files.createDirectories(directory);

                imagesPost = Arrays.stream(files)
                        .map(file -> ImagePost.builder()
                                .imageName(file.getOriginalFilename())
                                .imagePath(uploadDir)
                                .build())
                        .collect(Collectors.toList());
                for (int i = 0; i < files.length; i++) {
                    Path filePath = directory.resolve(imagesPost.get(i).getId().toString());
                    Files.write(filePath, files[i].getBytes());
                }
            }

            Group group = groupRepository.findById(groupId)
                    .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

            GroupPost post = groupPostRepository
                    .save(GroupPost.builder().caption(groupPostDto.getCaption()).imagePost(imagesPost).authorId(
                            userIdSecurity.getUId()).build());
            group.getGroupPostIds().add(post.getId());
            groupRepository.save(group);
            return ApiResponse.success(null, "Post created successfully", HttpStatus.CREATED);
        } catch (IOException e) {
            LoggerApp.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
// update the grop and add post