package com.webapp.madrasati.school_group.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.school_group.model.GroupPost;
import com.webapp.madrasati.school_group.model.ImagePost;
import com.webapp.madrasati.school_group.model.LikePost;
import com.webapp.madrasati.school_group.repository.ImagePostRepository;
import com.webapp.madrasati.school_group.repository.LikePostRepository;

import org.bson.types.ObjectId;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school_group.model.CommentPost;
import com.webapp.madrasati.school_group.model.Group;
import com.webapp.madrasati.school_group.repository.CommentPostRepository;
import com.webapp.madrasati.school_group.repository.GroupPostRepository;
import com.webapp.madrasati.school_group.repository.GroupRepository;
import com.webapp.madrasati.util.LocalFileStorageService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DeletePostService {
    private final GroupPostRepository postRepository;
    private final GroupRepository groupRepository;
    private final ImagePostRepository imageRepository;
    private final LocalFileStorageService fileStorageService;
    private final LikePostRepository likeRepository;
    private final CommentPostRepository commentRepository;

    @Async("taskExecutor")
    public void deletePost(String postIdString, String groupIdString) {
        ObjectId groupId = new ObjectId(groupIdString);
        ObjectId postId = new ObjectId(postIdString);
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        GroupPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!group.getGroupPostIds().contains(postId)) {
            throw new ResourceNotFoundException("Post not found in group");
        }
        if (!post.getImagePost().isEmpty()) {
            List<ImagePost> images = post.getImagePost().stream()
                    .map(imageId -> imageRepository.findById(imageId).orElse(null))
                    .filter(Objects::nonNull).toList();
            images.forEach(image -> deletePostImage(groupId, postId, image));
        }

        if (!post.getLikePost().isEmpty()) {
            List<LikePost> likes = post.getLikePost().stream()
            .map(likeId -> likeRepository.findById(likeId).orElse(null))
            .filter(Objects::nonNull).toList();
            likes.forEach(like -> likeRepository.delete(like));
        }

        if (!post.getCommentPost().isEmpty()) {
            List<CommentPost> comments = post.getCommentPost().stream()
                    .map(commentId -> commentRepository.findById(commentId).orElse(null))
                    .filter(Objects::nonNull).toList();
            comments.forEach(comment -> commentRepository.delete(comment));
        }
        try {

            if (!group.getGroupPostIds().remove(postId)) {
                throw new InternalServerErrorException("Something went wrong while deleting post from Database");
            }

            groupRepository.save(group);

            if (fileStorageService.deleteFile("group", groupIdString, "post", postIdString)) {
                postRepository.deleteById(postId);
            }

        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong while deleting post: " + e);
        }

    }

    private void deletePostImage(ObjectId groupId, ObjectId postId, ImagePost image) {
        String className = "group";
        String classId = groupId.toString();
        String category = "post/" + postId.toString();

        Path postImageDir = Paths
                .get(fileStorageService.getFileUrl(className, classId, category, image.getImageName()));
        LoggerApp.debug("post image dir: " + postImageDir);

        if (!Files.exists(postImageDir)) {
            LoggerApp.debug("no image found");
            return;
        }
        if (fileStorageService.deleteFile(className, classId, category, image.getImageName())) {
            imageRepository.delete(image);
            LoggerApp.debug("image deleted");
        }

    }
}
