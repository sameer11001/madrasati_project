package com.webapp.madrasati.school_group.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.madrasati.school_group.model.dto.req.GroupPostDto;
import com.webapp.madrasati.school_group.service.PostService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostServiceImp implements PostService {

    CreatePostService createPostService;

    @Override
    public String createPost(MultipartFile[] files, GroupPostDto groupPostDto, String groupIdString) {

        return createPostService.createPost(files, groupPostDto, groupIdString);
    }

    @Override
    public String addComment(String postId, String comment, String userId) {
        return null;
    }

    @Override
    public String deletePost(String postId, String userId, String groupIdString) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePost'");
    }

    @Override
    public String editPost(String postId, String userId, String groupIdString, GroupPostDto groupPostDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'editPost'");
    }

    @Override
    public String deleteComment(String commentId, String userId, String postId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteComment'");
    }

    @Override
    public String addLike(String postId, String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addLike'");
    }

    @Override
    public String removeLike(String postId, String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeLike'");
    }

}

// TODOD Implement all methods