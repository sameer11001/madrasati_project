package com.webapp.madrasati.school.model.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchoolEditResponseDto {
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

}
