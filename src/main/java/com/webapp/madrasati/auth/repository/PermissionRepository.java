package com.webapp.madrasati.auth.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.webapp.madrasati.auth.model.Permission;
import com.webapp.madrasati.core.repository.BaseRepository;

@Repository
public interface PermissionRepository extends BaseRepository<Permission, UUID> {
    Optional<Permission> findByPermissionName(String permissionName);
}
