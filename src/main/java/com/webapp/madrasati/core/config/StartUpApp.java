package com.webapp.madrasati.core.config;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.webapp.madrasati.auth.model.Permission;
import com.webapp.madrasati.auth.model.Role;
import com.webapp.madrasati.auth.model.UserEntity;

import com.webapp.madrasati.auth.repository.UserRepository;
import com.webapp.madrasati.auth.service.PermissionService;
import com.webapp.madrasati.auth.service.RoleService;

import com.webapp.madrasati.auth.util.RoleAppConstant;

import jakarta.transaction.Transactional;

@Component
public class StartUpApp implements CommandLineRunner {
        private final String adminEmail;
        private final String adminPassword;
        private UserRepository userRepository;
        private RoleService roleService;
        private PermissionService permissionService;
        private PasswordEncoder passwordEncoder;
        private static final Map<String, Set<String>> ROLE_PERMISSIONS = new HashMap<>();

        StartUpApp(@Value("${admin.email}") String adminEmail,
                        @Value("${admin.password}") String adminPassword,
                        UserRepository userRepository, RoleService roleService,
                        PermissionService permissionService,
                        PasswordEncoder passwordEncoder) {
                this.passwordEncoder = passwordEncoder;
                this.roleService = roleService;
                this.permissionService = permissionService;
                this.userRepository = userRepository;
                this.adminEmail = adminEmail;
                this.adminPassword = adminPassword;
        }

        static {
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

        @Transactional
        @Override
        public void run(String... args) {
                try {
                        Map<String, Permission> allPermissions = createPermissions();
                        createRoles(allPermissions);

                        // Create admin user
                        UserEntity admin = UserEntity.builder()
                                        .userEmail(adminEmail)
                                        .userFirstName("admin")
                                        .userPassword(passwordEncoder.encode(adminPassword))
                                        .userLastName("1")
                                        .userImage("null")
                                        .userGender('M')
                                        .userBirthDate(Date.valueOf("2000-01-01"))
                                        .userRole(roleService.findByRoleName(RoleAppConstant.ADMIN.getString()).get())
                                        .build();

                        // Create student user
                        UserEntity student = UserEntity.builder()
                                        .userEmail("student@madrasati")
                                        .userFirstName("student")
                                        .userPassword(passwordEncoder.encode("123456789n"))
                                        .userLastName("1")
                                        .userImage("null")
                                        .userGender('M')
                                        .userBirthDate(Date.valueOf("2002-01-01"))
                                        .userRole(roleService.findByRoleName(RoleAppConstant.STUDENT.getString()).get())
                                        .build();
                        // Create school manager user
                        UserEntity schoolManager = UserEntity.builder()
                                        .userEmail("schoolManager@madrasati")
                                        .userFirstName("schoolManager")
                                        .userPassword(passwordEncoder.encode("123456789a"))
                                        .userLastName("1")
                                        .userImage("null")
                                        .userGender('M')
                                        .userBirthDate(Date.valueOf("2001-01-01"))
                                        .userRole(roleService.findByRoleName(RoleAppConstant.SMANAGER.getString())
                                                        .get())
                                        .build();

                        if (userRepository.findAll().isEmpty()) {
                                userRepository.saveAll(List.of(admin, student, schoolManager));
                                LoggerApp.info("Admin created");
                                LoggerApp.info("Student created");
                                LoggerApp.info("School Manager created");
                        }
                        LoggerApp.info("Users already created");

                } catch (Exception e) {
                        LoggerApp.error("Error At Startup Class application: " + e.getMessage(), e);

                }
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
