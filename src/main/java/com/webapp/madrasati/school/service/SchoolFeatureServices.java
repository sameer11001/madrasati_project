package com.webapp.madrasati.school.service;

public interface SchoolFeatureServices {
    String addFeedBack(String feedBack, String schoolIdString);
    String rateSchool(Integer rateNumber, String schoolIdString);
    
}
