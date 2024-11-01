package com.webapp.madrasati.school_group.service;

import java.util.List;

import com.webapp.madrasati.school_group.model.dto.res.CommentAddBodyDto;
import com.webapp.madrasati.school_group.model.dto.res.EditPostBodyDto;
import com.webapp.madrasati.school_group.model.dto.res.PostPageBodyDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.madrasati.school_group.model.CommentPost;
import com.webapp.madrasati.school_group.model.dto.req.CommentReqDto;
import com.webapp.madrasati.school_group.model.dto.req.EditPostDto;
import com.webapp.madrasati.school_group.model.dto.res.PostResponseBodyDto;

public interface PostService {

    Page<PostPageBodyDto> getAllPosts(String groupIdString, int page, int size);

    PostResponseBodyDto createPost(List<MultipartFile> files, String caption, String groupIdString);

    void deletePost(String postIdString, String groupIdString);

    EditPostBodyDto editPost(String postId, EditPostDto body);

    CommentAddBodyDto addComment(CommentReqDto commentReqDto, String postIdString);

    void deleteComment(String commentIdString, String postIdString);

    String addLike(String postIdString);

    void removeLike(String postIdString);

}
