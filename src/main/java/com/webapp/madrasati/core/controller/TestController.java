package com.webapp.madrasati.core.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.madrasati.core.model.ApiResponse;
import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.service.SchoolService;

import org.springframework.web.bind.annotation.PostMapping;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/v1/test")
@Profile("dev")
@Lazy
public class TestController {
    @Autowired
    SchoolService schoolService;

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
        List<School> schools = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            schools.add(School.builder()
                    .schoolName("School " + i)
                    .schoolCoverImage(null)
                    .schoolAddress("Jordan")
                    .schoolEmail("school" + i + "@gmail.com")
                    .schoolDescription("School " + i + " description")
                    .schoolLocation("Amman")
                    .schoolPhoneNumber("077201777" + "" + i)
                    .schoolStudentCount(1000 + i)
                    .schoolType(i % 2 == 0 ? "High School" : "Elementary School")
                    // year 2001 - 2010 in milliseconds
                    .schoolFound(new Date(
                            (long) (2000 + i) * 365 * 24 * 60 * 60 * 1000))
                    .build());

        }
        schoolService.insertAll(schools);
        return null;
    }
}
