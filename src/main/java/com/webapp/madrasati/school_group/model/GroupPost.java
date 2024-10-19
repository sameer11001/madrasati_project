package com.webapp.madrasati.school_group.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bson.types.ObjectId;
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
@CompoundIndex(name = "author_group_index", def = "{'authorId': 1, 'groupId': 1}")
public class GroupPost extends BaseCollection {

    private UUID authorId;

    private String caption;

    private ObjectId groupId;

    @Builder.Default
    private List<ObjectId> imagePost = new ArrayList<>();

    @Builder.Default
    private List<ObjectId> commentPost = new ArrayList<>();

    @Builder.Default
    private List<ObjectId> likePost = new ArrayList<>();

}
