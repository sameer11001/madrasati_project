package com.webapp.madrasati.school_group.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.webapp.madrasati.school_group.repository.ImagePostRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.madrasati.auth.security.UserIdSecurity;
import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school_group.model.Group;
import com.webapp.madrasati.school_group.model.GroupPost;
import com.webapp.madrasati.school_group.model.ImagePost;
import com.webapp.madrasati.school_group.model.dto.res.PostResponseBodyDto;
import com.webapp.madrasati.school_group.repository.GroupPostRepository;
import com.webapp.madrasati.school_group.repository.GroupRepository;

@Service
public class CreatePostService {
    private final GroupPostRepository groupPostRepository;
    private final GroupRepository groupRepository;
    private final ImagePostRepository imagePostRepository;
    private final UserIdSecurity userId;
    private final String location;

    public CreatePostService(GroupPostRepository groupPostRepository, GroupRepository groupRepository,
            UserIdSecurity userIdSecurity, ImagePostRepository imagePostRepository,
            @Value("${upload_dir}") String location) {
        this.location = location;
        this.userId = userIdSecurity;
        this.groupPostRepository = groupPostRepository;
        this.groupRepository = groupRepository;
        this.imagePostRepository = imagePostRepository;
    }

    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<PostResponseBodyDto> createPost(MultipartFile[] files, String caption,
            String groupIdString) {

        ObjectId groupId = new ObjectId(groupIdString);
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        try {
            List<ImagePost> imagesPost = new ArrayList<>();
            if (files.length != 0) {
                final String uploadDir = location + "images/groups/" + groupId + "/posts";
                Path directory = Paths.get(uploadDir);

                Files.createDirectories(directory);

                imagesPost = Arrays.stream(files)
                        .map(file -> ImagePost.builder()
                                .imageName(file.getOriginalFilename())
                                .imagePath(uploadDir)
                                .build())
                        .toList();

                imagePostRepository.saveAll(imagesPost);

                for (int i = 0; i < files.length; i++) {
                    Path filePath = directory.resolve(imagesPost.get(i).getId().toString());
                    Files.write(filePath, files[i].getBytes());
                }

            }
            GroupPost post = groupPostRepository.save(
                    GroupPost.builder()
                            .caption(caption)
                            .imagePost(imagesPost)
                            .authorId(userId.getUId()).build());

            group.getGroupPostIds().add(post.getId());

            groupRepository.save(group);

            PostResponseBodyDto postResponseBodyDto = PostResponseBodyDto.builder().authorId(post.getAuthorId())
                    .caption(post.getCaption())
                    .imagePost(post.getImagePost())
                    .build();

            return CompletableFuture.completedFuture(postResponseBodyDto);

        } catch (IOException | IllegalArgumentException e) {
            throw new InternalServerErrorException("Something went wrong while creating post", e);
        }
    }
}