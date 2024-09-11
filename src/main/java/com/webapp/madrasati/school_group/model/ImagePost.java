package com.webapp.madrasati.school_group.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.webapp.madrasati.core.model.BaseCollection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "image_post")
public class ImagePost extends BaseCollection {

    private String imagePath;
    private String imageName;
}
