package com.webapp.madrasati.auth.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEditPassword {

    @NotEmpty(message = "old password must not be empty")
    @NotBlank
    private String oldPassword;

    @NotEmpty(message = "new password must not be empty")
    @NotBlank
    private String newPassword;
}
