package com.webapp.madrasati.school_group.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.core.index.CompoundIndex;
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
@CompoundIndex(name = "author_index", def = "{'authorId': 1}")
public class GroupPost extends BaseCollection {

    private UUID authorId;

    private String caption;

    @Builder.Default
    private List<ImagePost> imagePost = new ArrayList<>();

    @Builder.Default
    private List<CommentPost> commentPost = new ArrayList<>();

    @Builder.Default
    private List<LikePost> likePost = new ArrayList<>();

}
