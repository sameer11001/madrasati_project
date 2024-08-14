package com.webapp.madrasati.auth.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.webapp.madrasati.auth.model.Permission;
import com.webapp.madrasati.auth.model.Role;
import com.webapp.madrasati.auth.service.PermissionService;
import com.webapp.madrasati.auth.service.RoleService;
import com.webapp.madrasati.auth.util.RoleAppConstant;

import jakarta.annotation.PostConstruct;

@Component
public class RolePermissionSeeder {

    RoleService roleService;
    PermissionService permissionService;

    public RolePermissionSeeder(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    private static final Map<String, Set<String>> ROLE_PERMISSIONS = new HashMap<>();

    // This is permission mapping for each role
    static {

        // Admin role //TODO REVEIEW THIS PERMISSION
        ROLE_PERMISSIONS.put(RoleAppConstant.ADMIN.getString(), Set.of(
                "ADMIN_ACCESS", "STUDENT_ACCESS", "SCHOOL_MANAGER_ACCESS",
                "MANAGE_SCHOOL", "MANAGE_POSTS", "MANAGE_EVENTS",
                "VIEW_SCHOOL_INFO", "VIEW_EVENTS", "VIEW_POSTS",
                "EVENT_CREATE", "EVENT_READ", "EVENT_UPDATE", "EVENT_DELETE",
                "POST_CREATE", "POST_READ", "POST_UPDATE", "POST_DELETE"));
        // School Manager role
        ROLE_PERMISSIONS.put(RoleAppConstant.SMANAGER.getString(),
                Set.of("SCHOOL_MANAGER_ACCESS", "MANAGE_SCHOOL", "MANAGE_POSTS",
                        "MANAGE_EVENTS",
                        "VIEW_SCHOOL_INFO", "VIEW_EVENTS", "VIEW_POSTS",
                        "EVENT_CREATE", "EVENT_READ", "EVENT_UPDATE", "EVENT_DELETE",
                        "POST_CREATE", "POST_READ", "POST_UPDATE", "POST_DELETE"));
        // Student role
        ROLE_PERMISSIONS.put(RoleAppConstant.STUDENT.getString(), Set.of(
                "STUDENT_ACCESS", "VIEW_SCHOOL_INFO", "VIEW_EVENTS", "VIEW_POSTS",
                "EVENT_READ", "POST_READ"));
    }

    @PostConstruct
    public void seed() {
        Map<String, Permission> allPermissions = createPermissions();
        createRoles(allPermissions);
    }

    private Map<String, Permission> createPermissions() {
        return ROLE_PERMISSIONS.values().stream()
                .flatMap(Set::stream)
                .distinct()
                .map(this::createPermissionIfNotFound)
                .collect(Collectors.toMap(Permission::getPermissionName, permission -> permission));
    }

    private void createRoles(Map<String, Permission> allPermissions) {
        ROLE_PERMISSIONS.forEach((roleName, permissionNames) -> {
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

    private Role createRoleIfNotFound(String roleName, Set<Permission> permissions) {
        return roleService.findByRoleName(roleName)
                .orElseGet(() -> {
                    Role newRole = Role.builder()
                            .roleName(roleName)
                            .permissions(permissions)
                            .build();
                    return roleService.createRole(newRole);
                });
    }
}