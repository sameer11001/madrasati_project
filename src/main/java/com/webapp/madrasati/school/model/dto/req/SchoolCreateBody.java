package com.webapp.madrasati.school.model.dto.req;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchoolCreateBody {

    private String schoolName;
    private String schoolEmail;
    private String schoolPhoneNumber;
    private String schoolAddress;
    private String schoolLocation;
    private Integer schoolStudentCount;
    private String schoolType;
    private LocalDate schoolFound;
    private String schoolDescription;
    private String schoolMangerPassword;
}
