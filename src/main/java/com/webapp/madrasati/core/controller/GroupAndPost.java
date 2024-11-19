package com.webapp.madrasati.core.controller;

import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.service.UserServices;
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
import java.util.Random;

@Profile("dev")
@AllArgsConstructor
@RestController
public class GroupAndPost {

    private final GroupRepository groupRepository;
    private final GroupPostRepository groupPostRepository;
    private final ImagePostRepository imagePostRepository;
    private final LikePostRepository likePostRepository;
    private final CommentPostRepository commentPostRepository;
    private final UserServices userServices;

    @PostMapping("/createPostDummyData/{groupIdString}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void generateDummyData(@PathVariable String groupIdString) {
        Group group = groupRepository.findById(AppUtilConverter.Instance.stringToObjectId(groupIdString))
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        List<ObjectId> groupPostIds = new ArrayList<>();
        List<UserEntity> users = userServices.getAllUsersBySchoolId(group.getSchoolId());


        UserEntity managerUser = users.stream()
                .filter(user -> user.getUserRole().getRoleName().equals("ROLE_SCHOOL_MANAGER"))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No user with role MANAGER found"));


        List<UserEntity> otherUsers = new ArrayList<>(users);
        otherUsers.remove(managerUser);

        Random random = new Random();

        for (int i = 1; i <= 10; i++) {
            GroupPost groupPost = GroupPost.builder()
                    .authorId(managerUser.getId())
                    .caption("Post caption " + i)
                    .groupId(group.getId())
                    .build();

            groupPostRepository.save(groupPost);
            groupPostIds.add(groupPost.getId());

            if (i % 2 == 0) {
                List<ObjectId> imagePostIds = new ArrayList<>();
                for (int j = 1; j <= 3; j++) {
                    ImagePost imagePost = ImagePost.builder()
                            .postId(groupPost.getId())
                            .imagePath("images/group/post/image" + j + ".jpg")
                            .imageName("Image" + "_" + j)
                            .build();
                    imagePostRepository.save(imagePost);
                    imagePostIds.add(imagePost.getId());
                }
                groupPost.setImagePost(imagePostIds);
            }

            List<ObjectId> likePostIds = new ArrayList<>();
            for (int k = 1; k <= 5; k++) {
                UserEntity randomUserForLike = otherUsers.get(random.nextInt(otherUsers.size()));
                LikePost likePost = LikePost.builder()
                        .postId(groupPost.getId())
                        .userId(randomUserForLike.getId())
                        .isLike(true)
                        .build();
                likePostRepository.save(likePost);
                likePostIds.add(likePost.getId());
            }
            groupPost.setLikePost(likePostIds);

            List<ObjectId> commentPostIds = new ArrayList<>();
            for (int l = 1; l <= 3; l++) {
                UserEntity randomUserForComment = otherUsers.get(random.nextInt(otherUsers.size()));
                CommentPost commentPost = CommentPost.builder()
                        .postId(groupPost.getId())
                        .userId(randomUserForComment.getId())
                        .comment("Comment " + l + " on post " + i)
                        .build();
                commentPostRepository.save(commentPost);
                commentPostIds.add(commentPost.getId());
            }
            groupPost.setCommentPost(commentPostIds);

            groupPostRepository.save(groupPost);
        }
        group.setGroupPostIds(groupPostIds);
        groupRepository.save(group);
    }
}
