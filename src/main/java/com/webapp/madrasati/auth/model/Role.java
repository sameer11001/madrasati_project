package com.webapp.madrasati.auth.model;

import java.util.HashSet;
import java.util.Set;

import com.webapp.madrasati.core.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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
@Table(name = "role")
public class Role extends BaseEntity {

    @Column(name = "role_name", nullable = false, length = 40)
    String roleName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_permission_map", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    @Builder.Default
    private Set<Permission> permissions = new HashSet<>();
}
