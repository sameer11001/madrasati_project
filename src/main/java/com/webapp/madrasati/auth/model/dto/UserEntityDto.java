package com.webapp.madrasati.auth.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDate userBirthDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private School userSchool;
}
