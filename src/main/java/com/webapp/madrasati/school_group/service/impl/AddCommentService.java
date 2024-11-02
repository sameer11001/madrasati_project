package com.webapp.madrasati.school_group.service.impl;

import com.webapp.madrasati.school_group.model.dto.res.CommentAddBodyDto;
import com.webapp.madrasati.util.AppUtilConverter;
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
    private static final AppUtilConverter dataConverter = AppUtilConverter.Instance;

    @Transactional
    public CommentAddBodyDto addComment(CommentReqDto commentReqDto, String postIdString) {
        ObjectId postId = dataConverter.stringToObjectId(postIdString);
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
            return CommentAddBodyDto.builder()
                    .commentId(dataConverter.objectIdToString(comment.getId()))
                    .comment(comment.getComment())
                    .authorId(dataConverter.uuidToString(comment.getUserId()))
                    .postId(dataConverter.objectIdToString(comment.getPostId()))
                    .createdAt(comment.getCreatedAt()).build();
        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong while adding comment: " + e);
        }
    }
}
