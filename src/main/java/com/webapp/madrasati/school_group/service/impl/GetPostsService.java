package com.webapp.madrasati.school_group.service.impl;

import java.util.Collections;

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

@Service
@AllArgsConstructor
public class GetPostsService {
        private final GroupRepository groupRepository;
        private final GroupPostRepository postRepository;

        @Transactional(readOnly = true)
        public Page<PostResponseBodyDto> getPosts(String groupIdString, int page, int size) {
                ObjectId groupId = new ObjectId(groupIdString);
                Group group = groupRepository.findById(groupId)
                                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
                if (page < 0 || size <= 0) {
                        throw new BadRequestException("Page must be positive and size must be greater than 0");
                }

                Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

                Page<GroupPost> groupPostsPage = postRepository.findByGroupId(group.getId(), pageable);

                return groupPostsPage.map(this::convertToDto);
        }

        private PostResponseBodyDto convertToDto(GroupPost groupPost) {
                return PostResponseBodyDto.builder()
                                .authorId(groupPost.getAuthorId())
                                .groupId(groupPost.getGroupId())
                                .caption(groupPost.getCaption())
                                .imagePost(groupPost.getImagePost())
                                .commentPost(groupPost.getCommentPost())
                                .likePost(groupPost.getLikePost())
                                .build();
        }


}
