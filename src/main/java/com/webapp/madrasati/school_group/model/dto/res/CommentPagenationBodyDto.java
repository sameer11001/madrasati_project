package com.webapp.madrasati.school_group.model.dto.res;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentPagenationBodyDto {
    private String commentId;
    private String comment;
    private String author;
    private String authorId;
    private LocalDateTime createdAt;
}
