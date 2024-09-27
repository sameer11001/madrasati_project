package com.webapp.madrasati.school_group.model.dto.res;

import java.util.List;
import java.util.UUID;

import com.webapp.madrasati.school_group.model.CommentPost;
import com.webapp.madrasati.school_group.model.ImagePost;
import com.webapp.madrasati.school_group.model.LikePost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseBodyDto {
    private UUID authorId;
    private String caption;
    private List<ImagePost> imagePost;
    private List<CommentPost> commentPost;
    private List<LikePost> likePost;
}
