package com.webapp.madrasati.school.model;

import java.util.Date;
import java.util.Set;

import com.webapp.madrasati.core.model.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "school")
public class School extends BaseEntity {

    @Column(name = "school_name", nullable = false, length = 100)
    private String schoolName;

    @Column(name = "school_cover_image", nullable = true)
    private String schoolCoverImage;

    @Column(name = "school_email", nullable = true, length = 100)
    private String schoolEmail;

    @Column(name = "school_phone_number", nullable = true, length = 20)
    private String schoolPhoneNumber;

    @Column(name = "school_address", nullable = true, length = 100)
    private String schoolAddress;

    @Column(name = "school_location", nullable = true, length = 100)
    private String schoolLocation;

    @Column(name = "school_student_count", nullable = true)
    private Integer schoolStudentCount;

    @Column(name = "school_type", nullable = true, length = 100)
    private String schoolType;

    @Column(name = "school_found", nullable = true)
    private Date schoolFound;

    @Column(name = "school_description", nullable = true)
    private String schoolDescription;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "school", targetEntity = SchoolImage.class, fetch = FetchType.LAZY)
    private Set<SchoolImage> schoolImages;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "school", targetEntity = SchoolRating.class, fetch = FetchType.LAZY)
    private Set<SchoolRating> schoolRatings;

    @Transient
    private Double averageRating;

    public Double getAverageRating() {
        return calculateAverageRating();
    }

    public Double calculateAverageRating() {
        if (schoolRatings == null || schoolRatings.isEmpty()) {
            return 0.0;
        }
        return schoolRatings.stream()
                .mapToInt(SchoolRating::getRating)
                .average()
                .orElse(0.0);
    }
}