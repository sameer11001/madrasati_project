package com.webapp.madrasati.school_group.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school_group.model.Group;
import com.webapp.madrasati.school_group.repository.GroupPostRepository;
import com.webapp.madrasati.school_group.repository.GroupRepository;

@Service
@Transactional
public class DeletePostService {
    private final GroupPostRepository postRepository;
    private final GroupRepository groupRepository;
    private final String location;

    public DeletePostService(GroupPostRepository postRepository, GroupRepository groupRepository,
            @Value("${upload_dir}") String location) {
        this.location = location;
        this.postRepository = postRepository;
        this.groupRepository = groupRepository;
    }

    @Async("taskExecutor")
    @Transactional(rollbackFor = { ResourceNotFoundException.class,
            IllegalArgumentException.class, InternalServerErrorException.class })
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
            String postImageDir = location + "images/groups/" + groupId + "/posts/" + postId;
            Path postDirectory = Paths.get(postImageDir);

            if (Files.exists(postDirectory)) {
                Files.walk(postDirectory)
                        .map(Path::toFile)
                        .forEach(file -> {
                            try {
                                Files.delete(file.toPath());
                            } catch (IOException e) {
                                throw new InternalServerErrorException(
                                        "Something went wrong while deleting post: " + e);
                            }
                        });

                Files.deleteIfExists(postDirectory);
                // TODO delete the images from database not just the file
            }
        } catch (IOException e) {
            throw new InternalServerErrorException("Something went wrong while deleting post: " + e);
        }
    }
}
