package com.webapp.madrasati.school_group.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.webapp.madrasati.core.model.BaseCollection;
import com.webapp.madrasati.school_group.repository.ImagePostRepository;
import com.webapp.madrasati.util.LocalFileStorageService;

import org.bson.types.ObjectId;
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
    private final LocalFileStorageService fileStorageService;

    public CreatePostService(GroupPostRepository groupPostRepository, GroupRepository groupRepository,
            UserIdSecurity userIdSecurity, ImagePostRepository imagePostRepository,
            LocalFileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
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


        GroupPost post = groupPostRepository.save(
                GroupPost.builder()
                        .groupId(groupId)
                        .caption(caption)
                        .authorId(userId.getUId()).build());

        String className = "group";
        String category = "post/" + post.getId().toString();
        try {
            List<ImagePost> imagesPost = new ArrayList<>();
            if (files.length != 0) {
                List<String> fileNames = fileStorageService.storeFiles(className, groupIdString, category , files);

                imagesPost = fileNames.stream()
                        .map(fileName -> ImagePost.builder()
                                .imageName(fileName)
                                .imagePath(
                                        fileStorageService.getFileUrl(className, groupIdString, category, fileName))
                                .build())
                        .toList();

                imagePostRepository.saveAll(imagesPost);
            }

            post.setImagePost(imagesPost.stream()
                    .map(BaseCollection::getId).toList());

            groupPostRepository.save(post);

            group.getGroupPostIds().add(post.getId());

            groupRepository.save(group);

            PostResponseBodyDto postResponseBodyDto = PostResponseBodyDto.builder()
                    .authorId(post.getAuthorId())
                    .caption(post.getCaption())
                    .imagePost(post.getImagePost())
                    .build();

            return CompletableFuture.completedFuture(postResponseBodyDto);

        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong while creating post", e);
        }
    }
}