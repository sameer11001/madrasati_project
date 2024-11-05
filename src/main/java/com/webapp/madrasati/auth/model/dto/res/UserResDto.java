package com.webapp.madrasati.auth.model.dto.res;

import java.time.LocalDate;

import com.webapp.madrasati.core.model.BaseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResDto {

    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String userImage;
    private LocalDate userBirthDate;
    private char userGender;
}
