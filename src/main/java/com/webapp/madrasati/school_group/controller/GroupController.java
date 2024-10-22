package com.webapp.madrasati.school_group.controller;

import com.webapp.madrasati.school_group.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.webapp.madrasati.core.model.ApiResponseBody;
import com.webapp.madrasati.school_group.model.CommentPost;
import com.webapp.madrasati.school_group.model.Group;
import com.webapp.madrasati.school_group.model.dto.req.CommentReqDto;
import com.webapp.madrasati.school_group.model.dto.req.CreatePostDto;
import com.webapp.madrasati.school_group.model.dto.res.PostResponseBodyDto;
import com.webapp.madrasati.school_group.service.GroupService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("v1/group")
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;

    private final PostService postServiceImp;

    @PostMapping("/createGroup")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseBody<Group> createGroup(@RequestParam("schoolId") String schoolId) {
        return ApiResponseBody.success(groupService.createGroup(schoolId), "Group created successfully",
                HttpStatus.CREATED);
    }

    @GetMapping("/{groupId}/post/getAllPosts")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseBody<Page<PostResponseBodyDto>> getAllPosts(
            @Parameter(description = "Page number", schema = @Schema(type = "integer", defaultValue = "0")) @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "Page size", schema = @Schema(type = "integer", defaultValue = "1")) @RequestParam(name = "size", defaultValue = "1") int size,
            @PathVariable("groupId") String groupIdString) {
        return ApiResponseBody.success(postServiceImp.getAllPosts(groupIdString, page, size),
                "Get All Posts Successfully", HttpStatus.OK);
    }

    @PostMapping(value = "/{groupId}/createPost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseBody<PostResponseBodyDto> createGroupPost(@ModelAttribute CreatePostDto body,
            @PathVariable("groupId") String groupId) {

        return ApiResponseBody.success(postServiceImp.createPost(body.getImages(), body.getCaption(), groupId),
                "Post created successfully",
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{groupId}/post/{postId}/deletePost")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponseBody<Void> deletePost(@PathVariable("groupId") String groupId,
            @PathVariable("postId") String postId) {
        postServiceImp.deletePost(postId, groupId);
        return ApiResponseBody.successWithNoData;
    }

    @PostMapping("/post/{postId}/addComment")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseBody<CommentPost> addComment(
            @Parameter(description = "Comment", required = true) @RequestBody @Valid CommentReqDto commentReqDto,
            @PathVariable("postId") String postId) {
        return ApiResponseBody.success(postServiceImp.addComment(commentReqDto, postId), "Comment added successfully",
                HttpStatus.CREATED);
    }

    @DeleteMapping("/post/{postId}/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponseBody<Void> deleteComment(@PathVariable("postId") String postId,
            @PathVariable("commentId") String commentId) {
        postServiceImp.deleteComment(commentId, postId);
        return ApiResponseBody.successWithNoData;
    }

    @PostMapping("/post/{postId}/addLike")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponseBody<Void> addLike(@PathVariable("postId") String postId) {
        postServiceImp.addLike(postId);
        return ApiResponseBody.successWithNoData;
    }

    @DeleteMapping("/post/{postId}/removeLike")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponseBody<Void> removeLike(@PathVariable("postId") String postId) {
        postServiceImp.removeLike(postId);
        return ApiResponseBody.successWithNoData;
    }

}
