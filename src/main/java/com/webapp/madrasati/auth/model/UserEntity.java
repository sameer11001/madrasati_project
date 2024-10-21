package com.webapp.madrasati.auth.model;

import java.time.LocalDate;

import com.webapp.madrasati.auth.util.GenderConstant;
import com.webapp.madrasati.core.model.BaseEntity;
import com.webapp.madrasati.school.model.School;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_entity", indexes = { @Index(name = "idx_user_email", columnList = "user_email") })
public class UserEntity extends BaseEntity {

    @NotBlank
    @NotNull
    @Column(name = "user_email", nullable = false, length = 75)
    private String userEmail;

    @NotBlank
    @NotNull
    @Column(name = "user_password", nullable = false, length = 100)
    private String userPassword;

    @NotBlank
    @NotNull
    @Column(name = "user_first_name", nullable = false, length = 100)
    private String userFirstName;

    @Column(name = "user_last_name", nullable = true, length = 100)
    private String userLastName;

    @Column(name = "user_image", nullable = true)
    private String userImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender", nullable = false)
    private GenderConstant userGender;

    @NotNull
    @Column(name = "user_birthday", nullable = false)
    private LocalDate userBirthDate;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_role_id")
    private Role userRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School userSchool;
}