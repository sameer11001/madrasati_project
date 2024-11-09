package com.webapp.madrasati.auth.model.dto.res;

import java.time.Instant;

import com.webapp.madrasati.school.repository.summary.SchoolSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginGuestResponseDto {

   private String Gid;

    private String username;

    private String accessToken;

    private String token;

    private Instant expiryDate;

    private Page<SchoolSummary> data;


}
