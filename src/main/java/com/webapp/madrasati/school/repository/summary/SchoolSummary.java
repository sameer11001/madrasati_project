package com.webapp.madrasati.school.repository.summary;

import java.util.UUID;

public interface SchoolSummary {

    UUID getId();

    String getSchoolName();

    String getSchoolCoverImage();

    Double getAverageRating();

    String getSchoolType();
}
