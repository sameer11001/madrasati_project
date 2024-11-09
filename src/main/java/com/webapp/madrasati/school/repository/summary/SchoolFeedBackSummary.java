package com.webapp.madrasati.school.repository.summary;

import java.time.LocalDateTime;
import java.util.UUID;

public interface SchoolFeedBackSummary {
    UUID getFeedbackId();
    String getFeedbackDescription();
    UUID getUserId();
    String getUserFirstName();
    LocalDateTime getCreatedAt();
}
