package com.webapp.madrasati.school_group.service.impl;

import com.webapp.madrasati.core.error.AlreadyExistException;
import com.webapp.madrasati.school_group.model.dto.res.LikeToggleResponseDto;
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

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ToggleLikeService {

    private final LikePostRepository likeRepository;
    private final GroupPostRepository postRepository;
    private final UserIdSecurity userId;
    private static final AppUtilConverter dataConverter = AppUtilConverter.Instance;

    @Transactional
    public LikeToggleResponseDto toggleLike(String postIdString) {
        ObjectId postId = dataConverter.stringToObjectId(postIdString);
        UUID uid = userId.getUId();

        GroupPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Optional<LikePost> existingLike = likeRepository.findByPostIdAndUserId(postId, uid);

        if (existingLike.isPresent()) {

            LikePost like = existingLike.get();
            post.getLikePost().removeIf(likePostId ->
                    likePostId != null
                            && uid.equals(like.getUserId())
                            && likePostId.equals(like.getId())
                            && like.isLike());
            postRepository.save(post);
            likeRepository.delete(like);
            return LikeToggleResponseDto.builder()
                    .isLiked(false).likeCount(post.getLikePost().size())
                    .postId(dataConverter.objectIdToString(post.getId()))
                    .likeCount(post.getLikePost().size())
                    .authorId(dataConverter.uuidToString(like.getUserId())).build();
        } else {

            LikePost like = LikePost.builder()
                    .userId(uid)
                    .postId(postId)
                    .isLike(true)
                    .build();
            likeRepository.save(like);
            post.getLikePost().add(like.getId());
            postRepository.save(post);
            return LikeToggleResponseDto.builder()
                    .isLiked(true)
                    .likeCount(post.getLikePost().size())
                    .postId(dataConverter.objectIdToString(post.getId()))
                    .authorId(dataConverter.uuidToString(like.getUserId())).build();
        }
    }
}
