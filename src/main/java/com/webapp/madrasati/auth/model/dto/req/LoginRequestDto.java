package com.webapp.madrasati.auth.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @NotBlank
    @NotEmpty(message = "user email must not be empty")
    private String userEmail;

    @NotBlank
    @NotEmpty(message = "password must not be empty")
    private String password;

}
