package com.webapp.madrasati.school_group.service.impl;

import java.util.UUID;

import com.webapp.madrasati.util.AppUtilConverter;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final CommentPostRepository commentRepository;

    private final GroupPostRepository postRepository;

    private final UserIdSecurity userId;

    private static final AppUtilConverter dataConverter = AppUtilConverter.Instance;

    @Transactional
    public void deleteComment(String postIdString, String commentIdString) {
        ObjectId postId = dataConverter.stringToObjectId(postIdString);
        ObjectId commentId = dataConverter.stringToObjectId(commentIdString);
        UUID uid = userId.getUId();

        GroupPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        CommentPost comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("there is no comment for this user"));
        try {
            post.getCommentPost().removeIf(commentPost -> commentPost != null
                    && commentPost.equals(commentId) && comment.getUserId().equals(uid));

            postRepository.save(post);

            commentRepository.delete(comment);

        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong while deleting comment: " + e);
        }
    }

}
