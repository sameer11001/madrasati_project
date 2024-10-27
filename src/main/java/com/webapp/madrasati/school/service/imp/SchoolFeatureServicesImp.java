package com.webapp.madrasati.school.service.imp;

import org.springframework.stereotype.Service;

import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.SchoolFeedBack;
import com.webapp.madrasati.school.service.SchoolFeatureServices;
import com.webapp.madrasati.school.service.SchoolService;


@Service
public class SchoolFeatureServicesImp implements SchoolFeatureServices {

    SchoolService schoolService;

    @Override
    public String addFeedBack(String feedBack, String schoolIdString) {
       School schoolEntity = schoolService.findById(schoolIdString);
       
       SchoolFeedBack schoolFeedBack =  SchoolFeedBack.builder()
       .feedbackDescription(feedBack)
       .school(schoolEntity)
       .build();

       schoolEntity.getSchoolFeedBacks().add(schoolFeedBack);

       return "FeedBack added successfully";

    }

    @Override
    public String rateSchool(Integer rateNumber, String schoolIdString) {
        return null;
    }
    
}
