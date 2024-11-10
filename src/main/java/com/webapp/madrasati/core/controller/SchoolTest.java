package com.webapp.madrasati.core.controller;

import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.service.UserServices;
import com.webapp.madrasati.core.config.LoggerApp;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.SchoolFeedBack;
import com.webapp.madrasati.school.model.SchoolImage;
import com.webapp.madrasati.school.repository.SchoolFeedBackRepository;
import com.webapp.madrasati.school.repository.SchoolImageRepository;
import com.webapp.madrasati.school.service.imp.SchoolServicesImp;
import com.webapp.madrasati.util.AppUtilConverter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@Profile("dev")
@AllArgsConstructor
public class SchoolTest {

    private final SchoolServicesImp schoolServicesImp;
    private final SchoolImageRepository schoolImageRepository;
    private static final AppUtilConverter dataConverter = AppUtilConverter.Instance;
    private final SchoolFeedBackRepository schoolFeedBackRepository;
    private final UserServices userServices;
    @PostMapping("addImages")
    @PreAuthorize("hasRole('ADMIN')")
    public void addImages(@RequestParam("schoolId") String schoolIdString) {
        UUID schoolId = dataConverter.stringToUUID(schoolIdString);
        School school = schoolServicesImp.findById(schoolId);

        Set<SchoolImage> schoolImages = new HashSet<>();
        for (int i = 1; i <= 5; i++) {
            SchoolImage schoolImage = SchoolImage.builder()
                    .imagePath("/images/school/school_images/school" + i +".jpg")
                    .imageName("image " + i + " View")
                    .school(school)
                    .build();
            schoolImages.add(schoolImage);
        }
        school.setSchoolImages(schoolImages);

        schoolImageRepository.saveAll(schoolImages);
        schoolServicesImp.saveSchool(school);

        LoggerApp.info("Images added successfully");

    }

    @PostMapping("addFeedBack")
    @PreAuthorize("hasRole('ADMIN')")
    public void addFeedBack(@RequestParam("schoolId") String schoolIdString) {
        UUID schoolId = dataConverter.stringToUUID(schoolIdString);
        School school = schoolServicesImp.findById(schoolId);
        List<UserEntity> users = userServices.getAllUsersBySchoolId(schoolId);

        UserEntity managerUser = users.stream()
                .filter(user -> user.getUserRole().getRoleName().equals("ROLE_SCHOOL_MANAGER"))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No user with role MANAGER found"));

        List<UserEntity> otherUsers = new ArrayList<>(users);
        otherUsers.remove(managerUser);

        Random random = new Random();

        List<SchoolFeedBack> schoolFeedBacks = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            UserEntity randomUserForLike = otherUsers.get(random.nextInt(otherUsers.size()));
            SchoolFeedBack schoolFeedBack = SchoolFeedBack.builder()
                    .feedbackDescription("feedback " + i)
                    .school(school)
                    .user(randomUserForLike)
                    .build();
            schoolFeedBacks.add(schoolFeedBack);
        }

        schoolFeedBackRepository.saveAll(schoolFeedBacks);

        school.setSchoolFeedBacks(schoolFeedBacks);
        schoolServicesImp.saveSchool(school);
        LoggerApp.info("FeedBack added successfully");
    }
}
