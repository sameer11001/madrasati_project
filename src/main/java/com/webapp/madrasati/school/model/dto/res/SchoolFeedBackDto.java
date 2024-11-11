package com.webapp.madrasati.school.model.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class SchoolFeedBackDto {
    private UUID feedbackId;
    private String feedbackDescription;
    private UUID userId;
    private String userFirstName;
    private LocalDateTime createdAt;
}
