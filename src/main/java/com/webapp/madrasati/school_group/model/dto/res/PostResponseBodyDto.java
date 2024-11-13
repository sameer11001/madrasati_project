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
    private String authorId;
    private String caption;
    private String groupId;
    private boolean withImage;
    private String schoolImagePath;
    private List<String> imagePost;
    private LocalDateTime createdAt;
}
