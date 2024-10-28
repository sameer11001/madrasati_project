package com.webapp.madrasati.school.service.imp;

import com.webapp.madrasati.auth.model.UserEntity;
import com.webapp.madrasati.auth.security.UserIdSecurity;
import com.webapp.madrasati.auth.service.UserServices;
import com.webapp.madrasati.core.error.InternalServerErrorException;
import com.webapp.madrasati.core.error.ResourceNotFoundException;
import com.webapp.madrasati.school.model.SchoolRating;
import com.webapp.madrasati.school.repository.SchoolFeedBackRepository;
import com.webapp.madrasati.school.repository.SchoolRatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.SchoolFeedBack;
import com.webapp.madrasati.school.service.SchoolFeatureServices;
import com.webapp.madrasati.school.service.SchoolService;


@Service
@AllArgsConstructor
public class SchoolFeatureServicesImp implements SchoolFeatureServices {

    private final SchoolService schoolService;
    private final SchoolFeedBackRepository schoolFeedBackRepository;
    private final SchoolRatingRepository schoolRatingRepository;
    private final UserServices userServices;
    private final UserIdSecurity userId;

    @Override
    public SchoolFeedBack addFeedBack(String feedBack, String schoolIdString) {
       School schoolEntity = schoolService.findById(schoolIdString);

       UserEntity userEntity = userServices.findByUserId(userId.getUId()).orElseThrow(()-> new ResourceNotFoundException("User not found"));

       SchoolFeedBack schoolFeedBack =  SchoolFeedBack.builder()
       .feedbackDescription(feedBack)
       .school(schoolEntity)
       .user(userEntity)
       .build();
        try {
        return schoolFeedBackRepository.saveAndFlush(schoolFeedBack);
        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong in saving Feedback : " + e.getMessage());
        }
    }

    @Override
    public SchoolRating rateSchool(Integer rateNumber, String schoolIdString) {
        School schoolEntity = schoolService.findById(schoolIdString);

        UserEntity userEntity = userServices.findByUserId(userId.getUId()).orElseThrow(()-> new ResourceNotFoundException("User not found"));

        SchoolRating schoolRating =  SchoolRating.builder()
                .rating(rateNumber).school(schoolEntity).ratingUser(userEntity).build();
        try {
            return schoolRatingRepository.saveAndFlush(schoolRating);
        } catch (Exception e) {
            throw new InternalServerErrorException("Something went wrong in add rating school : " + e.getMessage());
        }
    }

    
}
