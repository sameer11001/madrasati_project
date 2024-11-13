package com.webapp.madrasati.school_group.service.impl;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.webapp.madrasati.school_group.model.dto.res.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.school_group.model.dto.req.CommentReqDto;
import com.webapp.madrasati.school_group.model.dto.req.EditPostDto;
import com.webapp.madrasati.school_group.service.PostService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostServiceImp implements PostService {

    private final CreatePostService createPostService;
    private final AddCommentService addCommentService;
    private final AddLikeService addLikeService;
    private final DeletePostService deletePostService;
    private final RemoveLikeService removeLikeService;
    private final DeleteCommentService deleteCommentService;
    private final EditPostService editPostService;
    private final GetPostsService getPostsService;
    private final GetCommentPagenation commentPagenation;

    @Override
    public Page<PostPagenationBodyDto> getAllPosts(String groupIdString, int page, int size) {
        return getPostsService.getPosts(groupIdString, page, size);
    }

    @Override
    public PostResponseBodyDto createPost(List<MultipartFile> files, String caption, String groupIdString) {
        try {
            return createPostService.createPost(files, caption, groupIdString).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public void deletePost(String postIdString, String groupIdString) {
        deletePostService.deletePost(postIdString, groupIdString);
    }

    @Override
    public EditPostBodyDto editPost(String postId, EditPostDto body) {
        return editPostService.editPost(postId, body);
    }

    @Override
    public CommentAddBodyDto addComment(CommentReqDto commentReqDto, String postIdString) {
        return addCommentService.addComment(commentReqDto, postIdString);
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

    @Override
    public Page<CommentPagenationBodyDto> getCommentPagenation(String postIdString, int page, int size) {
        return commentPagenation.getCommentPagenation(postIdString, page, size);
    }

}
