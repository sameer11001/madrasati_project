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
public class GuestLogoutReqDto {

    @NotBlank
    @NotEmpty(message = "refresh token must not be empty")
    String refreshToken;

    @NotBlank
    @NotEmpty(message = "guid must not be empty")
    String Guid;
}
