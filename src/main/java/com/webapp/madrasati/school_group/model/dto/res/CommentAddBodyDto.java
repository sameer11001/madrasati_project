package com.webapp.madrasati.school_group.model.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentAddBodyDto {
    private String commentId;
    private String authorId;
    private String comment;
    private String postId;
    private LocalDateTime createdAt;
}
