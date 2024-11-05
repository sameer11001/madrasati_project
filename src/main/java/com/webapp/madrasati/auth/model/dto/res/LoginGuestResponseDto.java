package com.webapp.madrasati.auth.model.dto.res;

import java.time.Instant;
import java.util.UUID;

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

    UUID Gid;
    
    String username;

    private String accessToken;

    private String token;

    Instant expiryDate;

    Page<SchoolSummary> data;


}
