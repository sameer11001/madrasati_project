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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "school_feedback")
public class SchoolFeedBack extends BaseEntity {

    @Column(name = "feedback_description")
    private String feedbackDescription;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_user_id",referencedColumnName = "id")
    private UserEntity  user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_school_id",referencedColumnName = "id")
    private School school;

}