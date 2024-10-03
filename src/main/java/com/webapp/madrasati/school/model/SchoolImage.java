package com.webapp.madrasati.school.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webapp.madrasati.core.model.BaseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "school_image")
public class SchoolImage extends BaseEntity {

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "image_name")
    private String imageName;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id",referencedColumnName = "id")
    private School school;
}
