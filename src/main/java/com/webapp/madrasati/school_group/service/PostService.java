package com.webapp.madrasati.school_group.service;

import org.springframework.web.multipart.MultipartFile;

import com.webapp.madrasati.school_group.model.CommentPost;
import com.webapp.madrasati.school_group.model.dto.req.CommentReqDto;
import com.webapp.madrasati.school_group.model.dto.req.GroupPostDto;

public interface PostService {

    String createPost(MultipartFile[] files, GroupPostDto groupPostDto, String groupIdString);

    void deletePost(String postIdString, String groupIdString);

    String editPost(String postIdString, String groupIdString, GroupPostDto groupPostDto);

    CommentPost addComment(CommentReqDto commentReqDto);

    void deleteComment(String commentIdString, String postIdString);

    String addLike(String postIdString);

    void removeLike(String postIdString);

}
