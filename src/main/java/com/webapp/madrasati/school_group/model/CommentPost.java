package com.webapp.madrasati.school_group.model;

import java.util.UUID;

import org.bson.types.ObjectId;
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
@Document(collection = "comment_post")
public class CommentPost extends BaseCollection {

    private UUID userId;
    private ObjectId postId;
    private String comment;
}
