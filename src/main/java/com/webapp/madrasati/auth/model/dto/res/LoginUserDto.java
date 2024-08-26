package com.webapp.madrasati.auth.model.dto.res;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserDto {

    private String userEmail;
    private String firstName;
    private String lastName;
    private String imagePath;
    private Date birthDate;
    private char gender;
}
