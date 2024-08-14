package com.webapp.madrasati.auth.model;

import com.webapp.madrasati.core.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "permission")
public class Permission extends BaseEntity {

    @NotBlank
    @Column(name = "permission_name", nullable = false, length = 50)
    String permissionName;

}
