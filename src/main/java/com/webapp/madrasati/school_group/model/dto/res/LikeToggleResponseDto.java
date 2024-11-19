package com.webapp.madrasati.school_group.model.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeToggleResponseDto {

    private boolean isLiked;
    private int likeCount;
    private String postId;
    private String authorId;
}
