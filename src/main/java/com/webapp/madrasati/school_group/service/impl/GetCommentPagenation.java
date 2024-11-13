package com.webapp.madrasati.school_group.service.impl;

import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.service.UserServices;
import com.webapp.madrasati.school_group.model.CommentPost;
import com.webapp.madrasati.school_group.model.dto.res.CommentPagenationBodyDto;
import com.webapp.madrasati.school_group.repository.CommentPostRepository;
import com.webapp.madrasati.util.AppUtilConverter;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetCommentPagenation {

    private final CommentPostRepository commentPostRepository;
    private final UserServices userServices;

    public GetCommentPagenation(CommentPostRepository commentPostRepository, UserServices userServices) {
        this.commentPostRepository = commentPostRepository;
        this.userServices = userServices;
    }

    AppUtilConverter dataConverter = AppUtilConverter.Instance;

     public Page<CommentPagenationBodyDto> getCommentPagenation(String postIdString, int page, int size) {
         ObjectId postId = dataConverter.stringToObjectId(postIdString);

         Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

         Page<CommentPost> commentPostPage = commentPostRepository.findAllByPostId(postId, pageable);

         if (commentPostPage.isEmpty()) {
             return Page.empty(pageable);
         }

         return commentPostPage.map(this::convertToCommentPagenationBodyDto);

     }

     private CommentPagenationBodyDto convertToCommentPagenationBodyDto(CommentPost commentPost) {
         Optional<UserEntity> userEntity = userServices.findByUserId(commentPost.getUserId());
         if (userEntity.isEmpty()) {
             return null;
         }
         return CommentPagenationBodyDto.builder()
                 .author(userEntity.map(entity -> entity.getUserFirstName() + " " + entity.getUserLastName()).orElse("Unknown"))
                 .authorId(dataConverter.uuidToString(commentPost.getUserId()))
                 .commentId(dataConverter.objectIdToString(commentPost.getId()))
                 .comment(commentPost.getComment())
                 .createdAt(commentPost.getCreatedAt())
                 .build();
     }
}
