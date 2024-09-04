package com.webapp.madrasati.core.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.UrlResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.core.model.ApiResponse;
import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.service.SchoolService;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.PostMapping;

import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/v1/test")
@Profile("dev")
@Lazy
@Transactional
public class TestController {
    @Autowired
    SchoolService schoolService;

    private static final String LOCATION = "src\\main\\resources\\static\\school\\";
    private static final String FILENAME = "school_default.jpg";

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    @GetMapping("/admin_authority")
    @PreAuthorize("hasAuthority('READ_DATA')")
    public String adminAuthorityAccess() {
        return "Admin Authority Board.";
    }

    @GetMapping("/school_manager")
    @PreAuthorize("hasRole('SCHOOL_MANAGER')")
    public String adminAuthorityAccess2() {
        return "school Manger Authority Board2.";
    }

    @PostMapping("/create_schools")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> createSchools() {
        try {
            File file = new File(LOCATION + FILENAME);
            if (!file.exists()) {
                LoggerApp.debug("File not found");
            }
            Path path = Paths.get(file.toURI());

            UrlResource resource = new UrlResource(path.toUri());
            List<School> schools = new ArrayList<>();
            for (int i = 1; i <= 100; i++) {

                schools.add(School.builder()
                        .schoolName("School " + i)
                        .schoolCoverImage(
                                resource.getFilename())
                        .schoolAddress("Jordan")
                        .schoolEmail("school" + i + "@gmail.com")
                        .schoolDescription("School " + i + " description")
                        .schoolLocation("Amman")
                        .schoolPhoneNumber("077201777" + "" + i)
                        .schoolStudentCount(1000 + i)
                        .schoolType(i % 2 == 0 ? "High School" : "Elementary School")
                        .schoolFound(new Date(
                                (long) (1926 + i) * 365 * 24 * 60 * 60 * 1000))
                        .build());
            }
            return schoolService.insertAll(schools);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
