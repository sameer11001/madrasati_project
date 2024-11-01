package com.webapp.madrasati.school_group.service.impl;

import com.webapp.madrasati.util.AppUtilConverter;
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
public class AddLikeService {

    private final LikePostRepository likeRepository;
    private final GroupPostRepository postRepository;
    private final UserIdSecurity userId;

    @Transactional
    public String addLike(String postId) {
        ObjectId id = new ObjectId(postId);
        GroupPost post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        try {
            LikePost like = LikePost.builder()
                    .userId(userId.getUId())
                    .postId(id)
                    .isLike(true).build();

            likeRepository.save(like);

            post.getLikePost().add(like.getId());

            postRepository.save(post);

            return AppUtilConverter.Instance.objectIdToString(like.getId());

        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong while adding like: " + e);
        }
    }
}
