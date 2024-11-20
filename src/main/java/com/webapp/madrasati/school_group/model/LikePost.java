package com.webapp.madrasati.school_group.model;

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
@Document(collection = "like_post")
@CompoundIndex(name = "post_id_user_id_index", def = "{'postId': 1, 'userId': 1}", unique = true)
public class LikePost extends BaseCollection {
    private ObjectId postId;
    private UUID userId;
    private boolean isLike;

}
