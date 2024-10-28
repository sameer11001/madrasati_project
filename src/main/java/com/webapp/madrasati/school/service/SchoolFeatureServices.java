package com.webapp.madrasati.school.service;

import com.webapp.madrasati.school.model.SchoolFeedBack;
import com.webapp.madrasati.school.model.SchoolRating;

public interface SchoolFeatureServices {
    SchoolFeedBack addFeedBack(String feedBack, String schoolIdString);
    SchoolRating rateSchool(Integer rateNumber, String schoolIdString);
    
}
