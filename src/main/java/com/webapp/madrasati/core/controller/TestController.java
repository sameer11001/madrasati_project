package com.webapp.madrasati.core.controller;

import java.util.List;
import java.util.ArrayList;

import com.webapp.madrasati.auth.model.dto.UserEntityDto;
import com.webapp.madrasati.auth.service.UserServices;
import com.webapp.madrasati.auth.util.RoleAppConstant;
import com.webapp.madrasati.school.model.Teacher;
import com.webapp.madrasati.school.service.SchoolService;
import com.webapp.madrasati.school_group.model.Group;
import com.webapp.madrasati.school_group.service.GroupService;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.madrasati.auth.security.UserIdSecurity;
import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.school.model.School;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import java.time.LocalDate;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "v1/test")
@Profile("dev")
@Lazy
@AllArgsConstructor
public class TestController {

    private final SchoolService schoolService;

    private final UserIdSecurity userIdSecurity;

    private final UserServices userServices;

    private final GroupService groupService;

    private final PasswordEncoder passwordEncoder;

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
        return "Admin Board. Id :" + userIdSecurity.getUId().toString();
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
    @Transactional
    public String createSchools() {
        try {
            ClassPathResource pathResource = new ClassPathResource("static/images/school/school_default.jpg");
            if (!pathResource.exists()) {
                LoggerApp.debug("File not found");
                throw new InternalServerErrorException("School cover image file not found.");
            }

            List<School> schools = new ArrayList<>();

            Set<Teacher> teachers = Set.of(
                    Teacher.builder()
                            .teacherName("Teacher " + "A")
                            .teacherSubject("Math")
                            .teacherExperience(5)
                            .teacherDescription("Experienced Math teacher")
                            .teacherImage("/images/school/teachers/teacher_default.jpg")
                            .build(),
                    Teacher.builder()
                            .teacherName("Teacher " + "B")
                            .teacherSubject("Science")
                            .teacherExperience(3)
                            .teacherDescription("Passionate Science teacher")
                            .teacherImage("/images/school/teachers/teacher_default2.jpg")
                            .build());

            for (int i = 1; i <= 100; i++) {
                schools.add(School.builder()
                        .schoolName("School" + i)
                        .schoolCoverImage("/images/school/school_default.jpg")
                        .schoolAddress("Jordan")
                        .schoolEmail("school" + i + "@gmail.com")
                        .schoolDescription("School " + i + " description")
                        .schoolLocation("Amman")
                        .schoolPhoneNumber("077201777" + i)
                        .schoolStudentCount(1000 + i)
                        .schoolType(i % 2 == 0 ? "High School" : "Elementary School")
                        .schoolFound(LocalDate.of(1926 + i, 1, 1))
                        .teachers(teachers)
                        .build());
            }
            List<School> insertedSchools = schoolService.insertAll(schools);
            List<UserEntityDto> userSchoolList = createManagerUserList(insertedSchools);
            List<UserEntityDto> userStudentList = createStudentUserList(insertedSchools);
            List<Group> groups = createGroupsList(insertedSchools);

            if (userServices.insertAll(userSchoolList, RoleAppConstant.SMANAGER)
                    && userServices.insertAll(userStudentList, RoleAppConstant.STUDENT)
                    && groupService.insertAll(groups)) {
                LoggerApp.info("Successfully created schools");
                return "Successfully created schools";
            }else {
                return "Failed to create schools";
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            LoggerApp.error("Unexpected Error while school creation", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @GetMapping("/getAllSchool")
    @PreAuthorize("hasRole('ADMIN')")
    public List<School> getAllSchool() {
        try {
            return schoolService.getALLSchools().get();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            LoggerApp.error("Error fetching all schools", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    private List<Group> createGroupsList(List<School> schools) {
        return schools.stream()
                .map(school -> Group.builder().schoolId(school.getId()).SchoolImagePath(school.getSchoolCoverImage()).build())
                .toList();
    }

    private List<UserEntityDto> createManagerUserList(List<School> schools) {
        return schools.stream()
                .map(school -> UserEntityDto.builder()
                        .userEmail(school.getSchoolEmail())
                        .userFirstName(school.getSchoolName())
                        .userLastName("Manager")
                        .userPassword(passwordEncoder.encode(
                                "123456789n"))
                        .userGender('M')
                        .userBirthDate(school.getSchoolFound())
                        .userSchool(school)
                        .build())
                .toList();
    }

    private List<UserEntityDto> createStudentUserList(List<School> schools) {
        List<UserEntityDto> userStudentList = new ArrayList<>();
        for (School school : schools) {
            for (int i = 0; i < 10; i++) {
                userStudentList.add(UserEntityDto.builder()
                        .userEmail("student" + i + "@" + school.getSchoolName() + ".com")
                        .userFirstName("Student" + i)
                        .userLastName("STU")
                        .userPassword(passwordEncoder.encode(
                                "123456789n"))
                        .userGender('M')
                        .userBirthDate(LocalDate.of(2000 + i, 1, 1))
                        .userImage("/images/user/student/student.png")
                        .userSchool(school)
                        .build());
            }
        }

        return userStudentList;

    }
}
