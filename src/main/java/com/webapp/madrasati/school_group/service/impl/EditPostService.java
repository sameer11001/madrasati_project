package com.webapp.madrasati.school_group.service.impl;

import com.webapp.madrasati.school_group.model.dto.res.EditPostBodyDto;
import com.webapp.madrasati.util.AppUtilConverter;
import org.bson.types.ObjectId;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school_group.model.GroupPost;
import com.webapp.madrasati.school_group.model.dto.req.EditPostDto;
import com.webapp.madrasati.school_group.repository.GroupPostRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EditPostService {
    private final GroupPostRepository postRepository;

    private static final AppUtilConverter dataConverter = AppUtilConverter.Instance;

    @Transactional
    public EditPostBodyDto editPost(String postIdString, EditPostDto body) {
        ObjectId postId = dataConverter.stringToObjectId(postIdString);

        GroupPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

            post.setCaption(body.getCaption());
        try {
            GroupPost postModified = postRepository.save(post);

            return EditPostBodyDto.builder().authorId(dataConverter.uuidToString(postModified.getAuthorId()))
                    .caption(postModified.getCaption())
                    .imagePost(postModified.getImagePost().stream().map(dataConverter::objectIdToString).toList())
                    .updatedAt(postModified.getUpdatedAt())
                    .createdAt(postModified.getCreatedAt())
                    .build();

        } catch (IllegalArgumentException | DataAccessException e) {
            throw new InternalServerErrorException("Something went wrong while editing post: " + e);
        }
    }
}
