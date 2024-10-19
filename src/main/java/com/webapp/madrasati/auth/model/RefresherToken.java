package com.webapp.madrasati.auth.model;

import java.time.Instant;
import com.webapp.madrasati.core.model.BaseEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "refresher_token")
public class RefresherToken extends BaseEntity {

    @Column(name = "token")
    @NotBlank
    private String token;

    private Instant expiryDate;

    @OneToOne
    @JoinColumn(name = "device_id",referencedColumnName = "id")
    private UserDevice device;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
}
