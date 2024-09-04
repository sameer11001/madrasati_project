package com.webapp.madrasati.school.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Teacher {
    private String teacherName;
    private String teacherSubject;
    private Integer teacherExperience;
    private String teacherDescription;
    private String teacherImage;
}
