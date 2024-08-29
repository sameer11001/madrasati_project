package com.webapp.madrasati.school.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.madrasati.core.model.ApiResponse;
import com.webapp.madrasati.school.repository.SchoolSummary;
import com.webapp.madrasati.school.service.SchoolService;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/school")
public class SchoolController {

    SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping("/getAllSchools")
    public ApiResponse<Page<SchoolSummary>> getAllSchools(@RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "1") int size) {
        return schoolService.getSchoolHomePage(page, size);
    }

}
