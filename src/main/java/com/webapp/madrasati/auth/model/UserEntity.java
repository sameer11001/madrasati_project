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
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class UserEntity extends BaseEntity {

    @NotBlank
    @Pattern(regexp = "^\\d{10}$", message = "identifier must be 10 digit number")
    @Column(name = "user_email", nullable = false, length = 75)
    String userEmail;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$", message = "password must be min 8 and max 20 length containing atleast 1 uppercase, 1 lowercase, 1 special character and 1 digit ")
    @Column(name = "user_password", nullable = false, length = 50)
    String userPassword;

    @NotBlank
    @Column(name = "user_first_name", nullable = false, length = 100)
    String firstName;

    @Column(name = "user_last_name", nullable = true, length = 100)
    String lastName;

    @Column(name = "user_image", nullable = true)
    String userImage;

    @NotBlank
    @Column(name = "user_gender", nullable = false, length = 1)
    char userGender;

    @NotBlank
    @Column(name = "user_birthday", nullable = false)
    @Temporal(TemporalType.DATE)
    Date userBirthDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_role_id", referencedColumnName = "id")
    Role userRole;

}
