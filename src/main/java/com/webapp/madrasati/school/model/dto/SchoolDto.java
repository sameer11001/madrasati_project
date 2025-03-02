package com.webapp.madrasati.school.model.dto;

import java.util.Map;
import java.util.Set;

import java.time.LocalDate;

import com.webapp.madrasati.school.model.SchoolFeedBack;
import com.webapp.madrasati.school.model.SchoolImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchoolDto {
    private String schoolName;
    private String schoolCoverImage;
    private String schoolEmail;
    private String schoolPhoneNumber;
    private String schoolAddress;
    private String schoolLocation;
    private Integer schoolStudentCount;
    private String schoolType;
    private LocalDate schoolFound;
    private String schoolDescription;
    private Map<Integer, Double> averageRating;
    private Set<SchoolImage> schoolImages;
    private Set<SchoolFeedBack> schoolFeedBacks;
    private LocalDate createdAt;
    private LocalDate updatedAt;

}
