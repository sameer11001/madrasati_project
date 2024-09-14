package com.webapp.madrasati.school_group.service.impl;

import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.webapp.madrasati.auth.security.UserIdSecurity;
import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school_group.model.CommentPost;
import com.webapp.madrasati.school_group.model.GroupPost;
import com.webapp.madrasati.school_group.repository.CommentPostRepository;
import com.webapp.madrasati.school_group.repository.GroupPostRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DeleteCommentService {

    private CommentPostRepository commentRepository;

    private GroupPostRepository postRepository;

    private UserIdSecurity userId;

    public void deleteComment(String postIdString, String commentIdString) {
        ObjectId postId = new ObjectId(postIdString);
        ObjectId commentId = new ObjectId(commentIdString);
        UUID uid = userId.getUId();

        GroupPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        CommentPost comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        try {
            post.getCommentPost().removeIf(commentPost -> commentPost.getId() != null
                    && commentPost.getId().equals(commentId) && commentPost.getUserId().equals(uid));

            postRepository.save(post);

            commentRepository.delete(comment);

        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong while deleting comment: " + e.getMessage());
        }
    }

}
