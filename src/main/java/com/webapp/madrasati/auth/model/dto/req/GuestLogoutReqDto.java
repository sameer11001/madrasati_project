package com.webapp.madrasati.auth.model.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuestLogoutReqDto {
    String refreshToken;
    String Guid;
}
