package com.webapp.madrasati.auth.model.dto.req;

import java.time.LocalDate;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateUserBodyDto {
    private String userEmail;
    private String userPassword;
    private String userFirstName;
    private String userLastName;
    private String userImage;

    @Pattern(regexp = "[MF]", message = "Gender must be 'M' for male or 'F' for female")
    private char userGender;

    private LocalDate userBirthDate;
}