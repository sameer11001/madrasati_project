package com.webapp.madrasati.school.service.imp;

import org.springframework.stereotype.Service;

import com.webapp.madrasati.school.repository.SchoolFeedBackRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SchoolFeedBackService {
    private final SchoolFeedBackRepository schoolFeedBackRepository;

    
}
