package com.webapp.madrasati.core.config;

import java.net.MalformedURLException;
import java.time.LocalDate;

import com.webapp.madrasati.auth.service.RolePermissionService;
import com.webapp.madrasati.auth.util.RolePermissionConfig;
import com.webapp.madrasati.core.error.InternalServerErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.webapp.madrasati.auth.model.Role;
import com.webapp.madrasati.auth.model.UserEntity;

import com.webapp.madrasati.auth.repository.UserRepository;
import com.webapp.madrasati.auth.util.GenderConstant;
import com.webapp.madrasati.auth.util.RoleAppConstant;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Paths;

@Component
public class StartUpApp implements CommandLineRunner {
        private final String adminEmail;
        private final String adminPassword;
        private final RolePermissionService rolePermissionService;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final UrlResource resource;

        StartUpApp(@Value("${admin.email}") String adminEmail,
                        @Value("${admin.password}") String adminPassword,
                        UserRepository userRepository,
                        RolePermissionService rolePermissionService,
                        PasswordEncoder passwordEncoder) throws MalformedURLException {
                this.rolePermissionService = rolePermissionService;
                this.passwordEncoder = passwordEncoder;
                this.userRepository = userRepository;
                this.adminEmail = adminEmail;
                this.adminPassword = adminPassword;
                this.resource = new UrlResource(Paths.get("src/main/resources/static/images/user/avatar_default.jpg").toUri());
        }

        @Transactional
        @Override
        public void run(String... args) {
                try {
                        rolePermissionService.createRolesAndPermissions(RolePermissionConfig.ROLE_PERMISSIONS);

                        if (userRepository.findByUserEmail(adminEmail).isEmpty()) {
                                createUserIfNotFound(adminEmail, "admin", adminPassword, RoleAppConstant.ADMIN.getString(), LocalDate.parse("2000-01-01"));
                                createUserIfNotFound("student@madrasati", "student", "123456789n", RoleAppConstant.STUDENT.getString(), LocalDate.parse("2002-01-01"));
                                createUserIfNotFound("schoolManager@madrasati", "schoolManager", "123456789a", RoleAppConstant.SMANAGER.getString(), LocalDate.parse("2001-01-01"));
                        }
                } catch (Exception e) {
                    throw new InternalServerErrorException("Something went wrong while creating Roles and User");
                }

        }

        private void createUserIfNotFound(String email, String firstName, String password, String roleName, LocalDate birthDate) {
                Role role = rolePermissionService.findRoleByRoleName(roleName);
                if (userRepository.findByUserEmail(email).isEmpty()) {
                        UserEntity user = UserEntity.builder()
                                .userEmail(email)
                                .userFirstName(firstName)
                                .userLastName("1")
                                .userPassword(passwordEncoder.encode(password))
                                .userImage(resource.getFilename())
                                .userGender(GenderConstant.MALE)
                                .userBirthDate(birthDate)
                                .userRole(role)
                                .build();
                        userRepository.save(user);
                        LoggerApp.info(firstName + " user created");
                }
        }
}
