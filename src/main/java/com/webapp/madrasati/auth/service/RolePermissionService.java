package com.webapp.madrasati.auth.service.imp;

import com.webapp.madrasati.auth.model.Permission;
import com.webapp.madrasati.auth.model.Role;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class RolePermissionService {

    public void createRolesAndPermissions(Map<String, Set<String>> rolePermissionsMap) {
        Map<String, Permission> allPermissions = createPermissions(rolePermissionsMap);
        createRoles(rolePermissionsMap, allPermissions);
    }

    private Map<String, Permission> createPermissions(Map<String, Set<String>> rolePermissionsMap) {
        return Map.of();
    }

    private void createRoles(Map<String, Set<String>> rolePermissionsMap, Map<String, Permission> allPermissions) {

    }

    private Permission createPermissionIfNotFound(String permissionName) {
        return null;
    }

    private Role createRoleIfNotFound(String roleName, Set<Permission> permissions) {
        return null;
    }
}
