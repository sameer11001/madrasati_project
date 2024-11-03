package com.webapp.madrasati.school_group.service.impl;

import com.webapp.madrasati.auth.security.UserIdSecurity;
import com.webapp.madrasati.school_group.model.ImagePost;
import com.webapp.madrasati.school_group.model.LikePost;
import com.webapp.madrasati.school_group.model.dto.res.PostPageBodyDto;
import com.webapp.madrasati.school_group.repository.ImagePostRepository;
import com.webapp.madrasati.school_group.repository.LikePostRepository;
import com.webapp.madrasati.util.AppUtilConverter;
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
        private final ImagePostRepository imageRepository;
        private final UserIdSecurity userId;

        private static final AppUtilConverter dataConverter = AppUtilConverter.Instance;

        @Transactional(readOnly = true)
        public Page<PostPageBodyDto> getPosts(String groupIdString, int page, int size) {
                ObjectId groupId = dataConverter.stringToObjectId(groupIdString);
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

        private PostPageBodyDto convertToDto(GroupPost groupPost, Map<ObjectId, List<LikePost>> likesByPostId,Group group) {
                List<LikePost> postLikes = likesByPostId.getOrDefault(groupPost.getId(), List.of());
                UUID userUId = userId.getUId();
                boolean userLiked = userUId != null && postLikes.stream()
                        .anyMatch(like -> userUId.equals(like.getUserId()));

                return PostPageBodyDto.builder()
                        .authorId(dataConverter.uuidToString(groupPost.getAuthorId()))
                        .groupId(dataConverter.objectIdToString(groupPost.getGroupId()))
                        .caption(groupPost.getCaption())
                        .imagePost(groupPost.getImagePost().stream().map(this::getImagePath).toList())
                        .postId(AppUtilConverter.Instance.objectIdToString(groupPost.getId()))
                        .schoolImagePath(group.getSchoolImagePath())
                        .commentPost(groupPost.getCommentPost().stream().map(dataConverter::objectIdToString).toList())
                        .likePost(groupPost.getLikePost().stream().map(dataConverter::objectIdToString).toList())
                        .likeCount(groupPost.getLikePost() != null ? groupPost.getLikePost().size() : 0)
                        .commentCount(groupPost.getCommentPost() != null ? groupPost.getCommentPost().size() : 0)
                        .isUserLiked(userLiked)
                        .createdAt(groupPost.getCreatedAt())
                        .build();
        }
        private String getImagePath(ObjectId imageId) {
               ImagePost image = imageRepository.findById(imageId).orElseThrow(() -> new ResourceNotFoundException("Image not found"));

                return image.getImagePath();

        }
}
