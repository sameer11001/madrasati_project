package com.webapp.madrasati.school_group.service.impl;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school_group.model.GroupPost;
import com.webapp.madrasati.school_group.model.dto.req.EditPostDto;
import com.webapp.madrasati.school_group.model.dto.res.PostResponseBodyDto;
import com.webapp.madrasati.school_group.repository.GroupPostRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EditPostService {
    private final GroupPostRepository postRepository;

    @Transactional
    public PostResponseBodyDto editPost(String postIdString, EditPostDto body) {
        ObjectId postId = new ObjectId(postIdString);

        GroupPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        try {
            post.setCaption(body.getCaption());

            GroupPost postModified = postRepository.save(post);

            return PostResponseBodyDto.builder().authorId(postModified.getAuthorId())
                    .caption(postModified.getCaption())
                    .imagePost(postModified.getImagePost())
                    .commentPost(postModified.getCommentPost())
                    .likePost(postModified.getLikePost())
                    .build();

        } catch (IllegalArgumentException e) {
            throw new InternalServerErrorException("Something went wrong while editing post: " + e);
        }
    }
}
