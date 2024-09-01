package com.webapp.madrasati.school.model.dto.res;

import java.util.Set;
import com.webapp.madrasati.school.model.SchoolFeedBack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchoolPageDto {
    private String schoolName;
    private String schoolDescription;
    private String schoolPhoneNumber;
    private String schoolLocation;
    private Double averageRating;
    private String schoolEmail;
    private Set<SchoolFeedBack> schoolFeedBacks;

}
