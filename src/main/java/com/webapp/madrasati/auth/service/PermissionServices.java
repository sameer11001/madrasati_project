package com.webapp.madrasati.auth.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.webapp.madrasati.auth.model.Permission;
import com.webapp.madrasati.auth.repository.PermissionRepository;

@Service
public class PermissionServices {

    PermissionRepository permissionRepository;

    public PermissionServices(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    public Optional<Permission> findByPermissionName(String permissionName) {
        return permissionRepository.findByPermissionName(permissionName);
    }

}
