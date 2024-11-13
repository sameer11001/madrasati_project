package com.webapp.madrasati.school_group.model.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditPostBodyDto {
    private String authorId;
    private String caption;
    private List<String> imagePost;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
