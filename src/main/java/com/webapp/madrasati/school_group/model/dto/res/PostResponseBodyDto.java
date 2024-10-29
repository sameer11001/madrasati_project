package com.webapp.madrasati.school_group.model.dto.res;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseBodyDto {
    private UUID authorId;
    private String caption;
    private ObjectId groupId;
    private boolean withImage;
    private List<ObjectId> imagePost;
    private List<ObjectId> commentPost;
    private List<ObjectId> likePost;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
