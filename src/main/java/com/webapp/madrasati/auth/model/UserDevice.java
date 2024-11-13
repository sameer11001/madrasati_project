package com.webapp.madrasati.auth.model;

import com.webapp.madrasati.core.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_device")
public class UserDevice extends BaseEntity {

    @Column(name = "device_id", nullable = false, length = 50)
    String deviceId;

    @Column(name = "device_type", nullable = false, length = 50)
    String deviceType;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity userEntity;
}
