package com.webapp.madrasati.school_group.service.impl;

import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.madrasati.auth.security.UserIdSecurity;
import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school_group.model.GroupPost;
import com.webapp.madrasati.school_group.model.LikePost;
import com.webapp.madrasati.school_group.repository.GroupPostRepository;
import com.webapp.madrasati.school_group.repository.LikePostRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RemoveLikeService {

    private final LikePostRepository likeRepository;

    private final GroupPostRepository postRepository;

    private final UserIdSecurity userId;

    @Transactional
    public void removeLike(String postIdString) {
        ObjectId postId = new ObjectId(postIdString);
        UUID uid = userId.getUId();

        GroupPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        LikePost like = likeRepository.findByPostIdAndUserId(postId, uid)
                .orElseThrow(() -> new ResourceNotFoundException("Like not found"));
        try {
            post.getLikePost().removeIf(likePost -> likePost.getUserId() != null
                    && likePost.getUserId().equals(uid));

            postRepository.save(post);

            likeRepository.delete(like);

        } catch (IllegalArgumentException e) {
            throw new InternalServerErrorException("Something went wrong while removing like: " + e);
        }
    }
}
