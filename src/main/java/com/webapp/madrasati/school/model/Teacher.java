package com.webapp.madrasati.school.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Teacher implements Serializable {
    @Serial
    private static final long serialVersionUID = 1905122041950251207L;

    private String teacherName;
    private String teacherSubject;
    private Integer teacherExperience;
    private String teacherDescription;
    private String teacherImage;
}
