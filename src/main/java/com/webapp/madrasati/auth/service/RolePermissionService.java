package com.webapp.madrasati.auth.service;

import com.webapp.madrasati.auth.model.Permission;
import com.webapp.madrasati.auth.model.Role;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RolePermissionService {
    private final RoleServices roleService;
    private final PermissionServices permissionService;

    public Role findRoleByRoleName(String roleName) {
        return roleService.findByRoleName(roleName).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    public void createRolesAndPermissions(Map<String, Set<String>> rolePermissionsMap) {
        Map<String, Permission> allPermissions = createPermissions(rolePermissionsMap);
        createRoles(rolePermissionsMap, allPermissions);
    }

    private Map<String, Permission> createPermissions(Map<String, Set<String>> rolePermissionsMap) {
        return rolePermissionsMap.values().stream()
                .flatMap(Set::stream)
                .distinct()
                .map(this::createPermissionIfNotFound)
                .collect(Collectors.toMap(Permission::getPermissionName, permission -> permission));
    }

    private void createRoles(Map<String, Set<String>> rolePermissionsMap, Map<String, Permission> allPermissions) {
        rolePermissionsMap.forEach((roleName, permissionNames) -> {
            Set<Permission> rolePermissions = permissionNames.stream()
                    .map(allPermissions::get)
                    .collect(Collectors.toSet());
            createRoleIfNotFound(roleName, rolePermissions);
        });
    }

    private Permission createPermissionIfNotFound(String permissionName) {
        return permissionService.findByPermissionName(permissionName)
                .orElseGet(() -> {
                    Permission newPermission = Permission.builder()
                            .permissionName(permissionName)
                            .build();
                    return permissionService.createPermission(newPermission);
                });

    }

    private void createRoleIfNotFound(String roleName, Set<Permission> permissions) {
        roleService.findByRoleName(roleName)
                .orElseGet(() -> {
                    Role newRole = Role.builder()
                            .roleName(roleName)
                            .permissions(permissions)
                            .build();
                    return roleService.createRole(newRole);
                });
    }
}
