package com.webapp.madrasati.school_group.model.dto.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePostDto {

    @Size(max = 5, message = "You can upload a maximum of 5 images")
    private List<MultipartFile> images;

    @NotEmpty(message = "Caption is required")
    @Size(max = 255, message = "Caption must be less than 255 characters")
    private String caption;
}
