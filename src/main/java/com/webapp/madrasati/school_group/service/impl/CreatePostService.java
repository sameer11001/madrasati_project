package com.webapp.madrasati.school_group.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.webapp.madrasati.core.model.BaseCollection;
import com.webapp.madrasati.school_group.repository.ImagePostRepository;
import com.webapp.madrasati.util.AppUtilConverter;
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

    private static final AppUtilConverter dataConverter = AppUtilConverter.Instance;

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
    public CompletableFuture<PostResponseBodyDto> createPost(List<MultipartFile> files, String caption,
                                                             String groupIdString) {

        boolean withImage = false;
        ObjectId groupId = dataConverter.stringToObjectId(groupIdString);
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        GroupPost post = groupPostRepository.save(
                GroupPost.builder()
                        .groupId(groupId)
                        .caption(caption)
                        .authorId(userId.getUId()).build());

        String className = "group";
        String category = "post/" + dataConverter.objectIdToString(post.getId());
        try {
            List<ImagePost> imagesPost = new ArrayList<>();
            if (files != null && !files.isEmpty()) {
                List<String> fileNames = fileStorageService.storeFiles(className, groupIdString,
                        category, files);

                imagesPost = fileNames.stream()
                        .map(fileName -> ImagePost.builder()
                                .imageName(fileName)
                                .imagePath(
                                        fileStorageService.getTargetLocationTrimed(className,
                                                groupIdString, category,
                                                fileName))
                                .build())
                        .toList();

                imagePostRepository.saveAll(imagesPost);
                withImage = true;
            }

            post.setImagePost(imagesPost.stream()
                    .map(BaseCollection::getId).toList());
            groupPostRepository.save(post);

            group.getGroupPostIds().add(post.getId());

            groupRepository.save(group);

            PostResponseBodyDto postResponseBodyDto = PostResponseBodyDto.builder()
                    .authorId(AppUtilConverter.Instance.uuidToString(post.getAuthorId()))
                    .caption(post.getCaption())
                    .imagePost(post.getImagePost().stream().map(AppUtilConverter.Instance::objectIdToString).toList())
                    .withImage(withImage)
                    .build();

            return CompletableFuture.completedFuture(postResponseBodyDto);

        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong while creating post", e);
        }
    }
}