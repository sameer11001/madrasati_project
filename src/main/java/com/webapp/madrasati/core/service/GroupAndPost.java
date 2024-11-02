package com.webapp.madrasati.core.service;

import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school_group.model.*;
import com.webapp.madrasati.school_group.repository.*;
import com.webapp.madrasati.util.AppUtilConverter;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Profile("dev")
@AllArgsConstructor
@RestController
public class GroupAndPost {

    private GroupRepository groupRepository;
    private GroupPostRepository groupPostRepository;
    private ImagePostRepository imagePostRepository;
    private LikePostRepository likePostRepository;
    private CommentPostRepository commentPostRepository;

    @PostMapping("/createPostDummyData/{groupIdString}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void generateDummyData(@PathVariable String groupIdString) {
       Group group = groupRepository.findById(AppUtilConverter.Instance.stringToObjectId(groupIdString)).orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        List<ObjectId> groupPostIds = new ArrayList<>();

        // Generate 10 posts for this group
        for (int i = 1; i <= 10; i++) {
            UUID authorId = UUID.randomUUID();
            GroupPost groupPost = GroupPost.builder()
                    .authorId(authorId)
                    .caption("Post caption " + i)
                    .groupId(group.getId())
                    .build();

            // Save group post and add its ID to the groupPostIds list
            groupPostRepository.save(groupPost);
            groupPostIds.add(groupPost.getId());

            // Generate images for each post
            List<ObjectId> imagePostIds = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                ImagePost imagePost = ImagePost.builder()
                        .postId(groupPost.getId())
                        .imagePath("/images/group/post/image"+j+".jpg")
                        .imageName("Image" + "_" + j)
                        .build();
                imagePostRepository.save(imagePost);
                imagePostIds.add(imagePost.getId());
            }
            groupPost.setImagePost(imagePostIds);

            List<ObjectId> likePostIds = new ArrayList<>();
            for (int k = 0; k < 5; k++) {
                UUID userId = UUID.randomUUID();
                LikePost likePost = LikePost.builder()
                        .postId(groupPost.getId())
                        .userId(userId)
                        .isLike(true)
                        .build();
                likePostRepository.save(likePost);
                likePostIds.add(likePost.getId());
            }
            groupPost.setLikePost(likePostIds);

            List<ObjectId> commentPostIds = new ArrayList<>();
            for (int l = 0; l < 3; l++) {
                UUID userId = UUID.randomUUID();
                CommentPost commentPost = CommentPost.builder()
                        .postId(groupPost.getId())
                        .userId(userId)
                        .comment("Comment " + l + " on post " + i)
                        .build();
                commentPostRepository.save(commentPost);
                commentPostIds.add(commentPost.getId());
            }
            groupPost.setCommentPost(commentPostIds);

            groupPostRepository.save(groupPost);
        }

        // Update group with the list of groupPostIds
        group.setGroupPostIds(groupPostIds);
        groupRepository.save(group);
    }
}
