package com.webapp.madrasati.auth.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    // @Pattern(regexp = "^\\d{10}$", message = "identifier must be 10 digit
    // number")
    private String userEmail;

    @NotBlank
    private String password;
}
