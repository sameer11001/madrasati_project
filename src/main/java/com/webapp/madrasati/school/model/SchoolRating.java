package com.webapp.madrasati.school.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.core.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "school_rating")
public class SchoolRating extends BaseEntity {

    @Column(name = "rating")
    private Integer rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rating_user_id", referencedColumnName = "id")
    private UserEntity ratingUser;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rating_school_id", referencedColumnName = "id")
    private School school;
}
