package com.webapp.madrasati.school_group.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;

import com.webapp.madrasati.core.error.BadRequestException;
import com.webapp.madrasati.core.model.BaseCollection;
import com.webapp.madrasati.school_group.repository.ImagePostRepository;
import com.webapp.madrasati.util.AppUtilConverter;
import com.webapp.madrasati.util.LocalFileStorageService;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.madrasati.auth.security.UserIdSecurity;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school_group.model.Group;
import com.webapp.madrasati.school_group.model.GroupPost;
import com.webapp.madrasati.school_group.model.ImagePost;
import com.webapp.madrasati.school_group.model.dto.res.PostResponseBodyDto;
import com.webapp.madrasati.school_group.repository.GroupPostRepository;
import com.webapp.madrasati.school_group.repository.GroupRepository;

@Service
public class CreatePostService {

    private static final Logger logger = LoggerFactory.getLogger(CreatePostService.class);

    private final GroupPostRepository groupPostRepository;
    private final GroupRepository groupRepository;
    private final ImagePostRepository imagePostRepository;
    private final UserIdSecurity userIdSecurity;
    private final LocalFileStorageService fileStorageService;

    private static final AppUtilConverter dataConverter = AppUtilConverter.Instance;

    public CreatePostService(GroupPostRepository groupPostRepository, GroupRepository groupRepository,
                             UserIdSecurity userIdSecurity, ImagePostRepository imagePostRepository,
                             LocalFileStorageService fileStorageService) {
        this.groupPostRepository = groupPostRepository;
        this.groupRepository = groupRepository;
        this.imagePostRepository = imagePostRepository;
        this.userIdSecurity = userIdSecurity;
        this.fileStorageService = fileStorageService;
    }

    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<PostResponseBodyDto> createPost(List<MultipartFile> files, String caption,
                                                             String groupIdStr) {
        boolean withImage = false;
        ObjectId groupId = dataConverter.stringToObjectId(groupIdStr);

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        GroupPost post = GroupPost.builder()
                .groupId(groupId)
                .caption(caption)
                .authorId(userIdSecurity.getUId())
                .build();

        groupPostRepository.save(post);
        String category = "post/" + dataConverter.objectIdToString(post.getId());

        try {
            List<ImagePost> imagePosts = new ArrayList<>();
            if (files != null && !files.isEmpty()) {
                List<String> fileNames = fileStorageService.storeFiles("group", groupIdStr, category, files);
                imagePosts = fileNames.stream()
                        .map(fileName -> ImagePost.builder()
                                .postId(post.getId())
                                .imageName(fileName)
                                .imagePath(fileStorageService.getTargetLocationTrimed("group", groupIdStr, category, fileName))
                                .build())
                        .toList();

                imagePostRepository.saveAll(imagePosts);
                withImage = true;
            }

            post.setImagePost(imagePosts.stream().map(BaseCollection::getId).toList());
            groupPostRepository.save(post);

            group.getGroupPostIds().add(post.getId());
            groupRepository.save(group);

            PostResponseBodyDto response = PostResponseBodyDto.builder()
                    .authorId(dataConverter.uuidToString(post.getAuthorId()))
                    .caption(post.getCaption())
                    .imagePost(post.getImagePost().stream().map(dataConverter::objectIdToString).toList())
                    .withImage(withImage)
                    .build();

            return CompletableFuture.completedFuture(response);

        } catch (BadRequestException | IllegalArgumentException | ResourceNotFoundException |
                 DataIntegrityViolationException | TransactionSystemException | RejectedExecutionException e) {
           return CompletableFuture.failedFuture(e);
        }
    }
}
