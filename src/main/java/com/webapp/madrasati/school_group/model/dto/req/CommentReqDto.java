package com.webapp.madrasati.school_group.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentReqDto {

    @NotBlank
    private String comment;

    @NotBlank
    private String postId;
}
