package com.webapp.madrasati.auth.model.dto.res;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResDto {

    private String userEmail;
    private String firstName;
    private String lastName;
    private String imagePath;
    private LocalDate birthDate;
    private char gender;
}
