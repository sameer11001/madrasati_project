package com.webapp.madrasati.school_group.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.madrasati.school_group.model.CommentPost;
import com.webapp.madrasati.school_group.model.dto.req.CommentReqDto;
import com.webapp.madrasati.school_group.model.dto.req.GroupPostDto;
import com.webapp.madrasati.school_group.service.PostService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostServiceImp implements PostService {

    private CreatePostService createPostService;
    private AddCommentService addCommentService;
    private AddLikeService addLikeService;
    private DeletePostService deletePostService;
    private RemoveLikeService removeLikeService;
    private DeleteCommentService deleteCommentService;

    @Override
    public String createPost(MultipartFile[] files, GroupPostDto groupPostDto, String groupIdString) {

        return createPostService.createPost(files, groupPostDto, groupIdString);
    }

    @Override
    public CommentPost addComment(CommentReqDto commentReqDto) {
        return addCommentService.addComment(commentReqDto);
    }

    @Override
    public void deletePost(String postIdString, String groupIdString) {
        deletePostService.deletePost(postIdString, groupIdString);
    }

    @Override
    public String editPost(String postId, String groupIdString, GroupPostDto groupPostDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'editPost'");
    }

    @Override
    public void deleteComment(String commentIdString, String postIdString) {
        deleteCommentService.deleteComment(postIdString, commentIdString);
    }

    @Override
    public String addLike(String postId) {
        return addLikeService.addLike(postId);
    }

    @Override
    public void removeLike(String postIdString) {
        removeLikeService.removeLike(postIdString);
    }

}
