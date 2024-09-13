package com.webapp.madrasati.school_group.service;

import org.springframework.web.multipart.MultipartFile;

import com.webapp.madrasati.school_group.model.dto.req.GroupPostDto;

public interface PostService {

    String createPost(MultipartFile[] files, GroupPostDto groupPostDto, String groupIdString);

    String deletePost(String postId, String userId, String groupIdString);

    String editPost(String postId, String userId, String groupIdString, GroupPostDto groupPostDto);

    String addComment(String postId, String comment, String userId);

    String deleteComment(String commentId, String userId, String postId);

    String addLike(String postId, String userId);

    String removeLike(String postId, String userId);

}
