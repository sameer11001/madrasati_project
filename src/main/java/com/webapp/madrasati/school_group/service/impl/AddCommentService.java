package com.webapp.madrasati.school_group.service.impl;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.madrasati.auth.security.UserIdSecurity;
import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school_group.model.CommentPost;
import com.webapp.madrasati.school_group.model.GroupPost;
import com.webapp.madrasati.school_group.model.dto.req.CommentReqDto;
import com.webapp.madrasati.school_group.repository.CommentPostRepository;
import com.webapp.madrasati.school_group.repository.GroupPostRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AddCommentService {
    private final CommentPostRepository commentRepository;
    private final GroupPostRepository postRepository;
    private final UserIdSecurity userId;

    @Transactional
    public CommentPost addComment(CommentReqDto commentReqDto, String postIdString) {
        ObjectId postId = new ObjectId(postIdString);
        GroupPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        try {
            String commentText = commentReqDto.getComment();
    if (commentText == null || commentText.trim().isEmpty()) {
        throw new IllegalArgumentException("Comment cannot be empty");
    }
            CommentPost comment = CommentPost.builder()
                    .userId(userId.getUId())
                    .comment(commentReqDto.getComment())
                    .postId(postId).build();

            commentRepository.save(comment);

            post.getCommentPost().add(comment.getId());

            postRepository.save(post);

            return comment;
        } catch (IllegalArgumentException e) {
            throw new InternalServerErrorException("Something went wrong while adding comment: " + e);
        }
    }
}
