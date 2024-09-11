package com.webapp.madrasati.school_group.model;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import com.webapp.madrasati.core.model.BaseCollection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "group_posts")
public class GroupPost extends BaseCollection {

    private UUID authorId;
    private String caption;
    private List<ImagePost> imagePost;
    private List<CommentPost> commentPost;
    private List<LikePost> likePost;

}
