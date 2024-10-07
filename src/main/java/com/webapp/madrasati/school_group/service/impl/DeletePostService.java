package com.webapp.madrasati.school_group.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bson.types.ObjectId;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school_group.model.Group;
import com.webapp.madrasati.school_group.repository.GroupPostRepository;
import com.webapp.madrasati.school_group.repository.GroupRepository;
import com.webapp.madrasati.util.LocalFileStorageService;

@Service
@Transactional
public class DeletePostService {
    private final GroupPostRepository postRepository;
    private final GroupRepository groupRepository;
    private final LocalFileStorageService fileStorageService;

    public DeletePostService(GroupPostRepository postRepository, GroupRepository groupRepository,
            LocalFileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
        this.postRepository = postRepository;
        this.groupRepository = groupRepository;
    }

    @Async("taskExecutor")
    public void deletePost(String postIdString, String groupIdString) {
        ObjectId groupId = new ObjectId(groupIdString);
        ObjectId postId = new ObjectId(postIdString);
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        if (!group.getGroupPostIds().contains(postId)) {
            throw new ResourceNotFoundException("Post not found in group");
        }
        try {
            postRepository.deleteById(postId);

            group.getGroupPostIds().remove(postId);

            groupRepository.save(group);

            deletePostImages(groupId, postId);

        } catch (IllegalArgumentException e) {
            throw new InternalServerErrorException("Something went wrong while deleting post: " + e);
        }

    }

    private void deletePostImages(ObjectId groupId, ObjectId postId) {
        try {
            String className = "group";
            String classId = groupId.toString();
            String category = "post-images";

            Path postImageDir = Paths
                    .get(fileStorageService.getFileUrl(className, classId, category, postId.toString()));

            if (Files.exists(postImageDir)) {

                // Walk through the directory and delete all files
                Files.walk(postImageDir)
                        .map(Path::toFile)
                        .forEach(file -> {
                            String fileName = file.getName();
                            try {
                                fileStorageService.deleteFile(className, classId, category, fileName);
                            } catch (Exception e) {
                                throw new InternalServerErrorException("Could not delete file: " + fileName, e);
                            }
                        });
            }
        } catch (IOException e) {
            throw new InternalServerErrorException("Something went wrong while deleting post: " + e);
        }
    }
}
