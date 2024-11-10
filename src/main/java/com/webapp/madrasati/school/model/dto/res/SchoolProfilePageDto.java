package com.webapp.madrasati.school.model.dto.res;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.webapp.madrasati.school.model.Teacher;

import com.webapp.madrasati.school.repository.summary.SchoolFeedBackSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchoolProfilePageDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1905122041950251207L;

    private String schoolId;
    private String schoolName;
    private String schoolCoverImage;
    private String schoolDescription;
    private String schoolPhoneNumber;
    private Integer schoolStudentCount;
    private String schoolLocation;
    private Map<Integer, Double> averageRating;
    private String schoolEmail;
    private List<SchoolFeedBackSummary> schoolFeedBacks;
    private List<String> schoolImages;
    private Set<Teacher> teachers;
}
