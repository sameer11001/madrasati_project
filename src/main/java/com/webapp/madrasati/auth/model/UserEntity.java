package com.webapp.madrasati.auth.model;

import java.sql.Date;

import com.webapp.madrasati.core.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
@Table(name = "user_entity")
public class UserEntity extends BaseEntity {

    @NotBlank
    @NotNull
    @Column(name = "user_email", nullable = false, length = 75, unique = true)
    private String userEmail;

    @NotBlank
    @NotNull
    @Column(name = "user_password", nullable = false, length = 100)
    // @Pattern(regexp =
    // "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$",
    // message = "password must be min 8 and max 20 length containing atleast
    // 1uppercase, 1 lowercase, 1 special character and 1 digit")
    private String userPassword;

    @NotBlank
    @NotNull
    @Column(name = "user_first_name", nullable = false, length = 100)
    private String userFirstName;

    @Column(name = "user_last_name", nullable = true, length = 100)
    private String userLastName;

    @Column(name = "user_image", nullable = true)
    private String userImage;

    @NotNull
    @Column(name = "user_gender", nullable = false)
    private char userGender;

    @NotNull
    @Column(name = "user_birthday", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date userBirthDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_role_id")
    private Role userRole;

}
