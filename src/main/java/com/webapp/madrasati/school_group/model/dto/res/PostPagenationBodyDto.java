package com.webapp.madrasati.school_group.model.dto.res;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostPagenationBodyDto {
    private String authorId;
    private String caption;
    private String groupId;
    private boolean withImage;
    private String postId;
    private String schoolImagePath;
    private List<String> imagePost;
    private int likeCount;
    private int commentCount;
    private boolean isUserLiked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
