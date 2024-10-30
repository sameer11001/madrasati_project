package com.webapp.madrasati.school_group.service.impl;

import com.webapp.madrasati.auth.security.UserIdSecurity;
import com.webapp.madrasati.school_group.model.LikePost;
import com.webapp.madrasati.school_group.repository.LikePostRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.webapp.madrasati.core.error.BadRequestException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school_group.model.Group;
import com.webapp.madrasati.school_group.model.GroupPost;
import com.webapp.madrasati.school_group.model.dto.res.PostResponseBodyDto;
import com.webapp.madrasati.school_group.repository.GroupPostRepository;
import com.webapp.madrasati.school_group.repository.GroupRepository;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GetPostsService {
        private final GroupRepository groupRepository;
        private final GroupPostRepository postRepository;
        private final LikePostRepository likeRepository;
        private final UserIdSecurity userId;

        @Transactional(readOnly = true)
        public Page<PostResponseBodyDto> getPosts(String groupIdString, int page, int size) {
                ObjectId groupId = new ObjectId(groupIdString);
                Group group = groupRepository.findById(groupId)
                        .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

                if (page < 0 || size <= 0) {
                        throw new BadRequestException("Page must be non-negative and size must be greater than 0");
                }

                Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
                Page<GroupPost> groupPostsPage = postRepository.findByGroupId(group.getId(), pageable);

                if (groupPostsPage.isEmpty()) {
                        return Page.empty(pageable);
                }

                List<ObjectId> postIds = groupPostsPage.getContent().stream()
                        .map(GroupPost::getId)
                        .toList();

                List<LikePost> likes = likeRepository.findByPostIdIn(postIds);
                Map<ObjectId, List<LikePost>> likesByPostId = likes.stream()
                        .collect(Collectors.groupingBy(LikePost::getPostId));

                return groupPostsPage.map(groupPost -> convertToDto(groupPost, likesByPostId,group));
        }

        private PostResponseBodyDto convertToDto(GroupPost groupPost, Map<ObjectId, List<LikePost>> likesByPostId,Group group) {
                List<LikePost> postLikes = likesByPostId.getOrDefault(groupPost.getId(), List.of());
                UUID userUId = userId.getUId();
                boolean userLiked = userUId != null && postLikes.stream()
                        .anyMatch(like -> userUId.equals(like.getUserId()));

                return PostResponseBodyDto.builder()
                        .authorId(groupPost.getAuthorId())
                        .groupId(groupPost.getGroupId())
                        .caption(groupPost.getCaption())
                        .imagePost(groupPost.getImagePost())
                        .schoolImagePath(group.getSchoolImagePath())
                        .commentPost(groupPost.getCommentPost())
                        .likePost(groupPost.getLikePost())
                        .likeCount(groupPost.getLikePost() != null ? groupPost.getLikePost().size() : 0)
                        .commentCount(groupPost.getCommentPost() != null ? groupPost.getCommentPost().size() : 0)
                        .isUserLiked(userLiked)
                        .createdAt(groupPost.getCreatedAt())
                        .build();
        }
}
