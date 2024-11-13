package com.webapp.madrasati.school.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webapp.madrasati.core.model.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
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
@Table(name = "school", indexes = { @Index(name = "idx_school_name", columnList = "school_name") })
public class School extends BaseEntity {

    @Column(name = "school_name", nullable = false, length = 100, unique = true)
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
    private LocalDate schoolFound;

    @Column(name = "school_description", nullable = true)
    private String schoolDescription;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "school", targetEntity = SchoolImage.class, fetch = FetchType.LAZY)
    private Set<SchoolImage> schoolImages;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "school", targetEntity = SchoolRating.class, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<SchoolRating> schoolRatings;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "school", targetEntity = SchoolFeedBack.class, fetch = FetchType.LAZY)
    private List<SchoolFeedBack> schoolFeedBacks;

    @ElementCollection
    @CollectionTable(name = "school_teachers", joinColumns = @JoinColumn(name = "school_id"))
    private Set<Teacher> teachers;

    @Transient
    private Map<Integer, Double> averageRating;

    public Map<Integer, Double> getAverageRating() {
        return calculateAverageRating();
    }

    public Map<Integer, Double> calculateAverageRating() {
        Map<Integer, Long> starCounts = new HashMap<>();
    Map<Integer, Double> starPercentages = new HashMap<>();
    
    // Initialize counts for each star rating from 1 to 5
    for (int i = 1; i <= 5; i++) {
        starCounts.put(i, 0L);
    }
    
    // Count occurrences of each rating
    schoolRatings.forEach(rating -> {
        int star = rating.getRating();
        if (star >= 1 && star <= 5) {  
            starCounts.put(star, starCounts.getOrDefault(star, 0L) + 1);
        }
    });
    
    // Calculate total ratings
    long totalRatings = schoolRatings.size();
    
    // Calculate percentage for each star rating
    for (Map.Entry<Integer, Long> entry : starCounts.entrySet()) {
        int star = entry.getKey();
        long count = entry.getValue();
        double percentage = totalRatings > 0 ? (double) count / totalRatings * 100 : 0.0; 
        starPercentages.put(star, percentage);
    }
    return starPercentages;
    }
}