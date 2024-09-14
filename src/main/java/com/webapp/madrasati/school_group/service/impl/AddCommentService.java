package com.webapp.madrasati.school_group.service.impl;

import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

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
    private CommentPostRepository commentRepository;
    private GroupPostRepository postRepository;
    private UserIdSecurity userId;

    public CommentPost addComment(CommentReqDto commentReqDto) {
        ObjectId postId = new ObjectId(commentReqDto.getPostId());
        GroupPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        try {
            CommentPost comment = CommentPost.builder()
                    .userId(userId.getUId())
                    .comment(commentReqDto.getComment())
                    .postId(postId).build();

            if (post.getCommentPost() == null) {
                post.setCommentPost(new ArrayList<>());
            }
            post.getCommentPost().add(comment);

            postRepository.save(post);

            commentRepository.save(comment);
            return comment;
        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong while adding comment: " + e.getMessage());
        }
    }
}
