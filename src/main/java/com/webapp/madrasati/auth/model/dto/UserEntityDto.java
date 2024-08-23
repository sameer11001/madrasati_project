package com.webapp.madrasati.auth.model.dto;

import java.sql.Date;
import java.time.LocalDate;

import com.webapp.madrasati.school.model.School;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntityDto {
    private String userEmail;
    private String userPassword;
    private String userFirstName;
    private String userLastName;
    private String userImage;
    private char userGender;
    private Date userBirthDate;
    private LocalDate createdDate;
    private LocalDate updatedDate;
    private School userSchool;
}
