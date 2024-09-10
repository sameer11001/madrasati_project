package com.webapp.madrasati.school.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.madrasati.core.model.ApiResponse;
import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.dto.req.SchoolCreateBody;
import com.webapp.madrasati.school.model.dto.res.SchoolPageDto;
import com.webapp.madrasati.school.repository.summary.SchoolSummary;
import com.webapp.madrasati.school.service.SchoolImageServices;
import com.webapp.madrasati.school.service.SchoolServices;

import lombok.AllArgsConstructor;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/school")
@AllArgsConstructor
public class SchoolController {

    private SchoolServices schoolService;

    private SchoolImageServices schoolImageServices;

    @GetMapping("/getAllSchools")
    public ApiResponse<Page<SchoolSummary>> getAllSchools(@RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "1") int size) {
        return schoolService.getSchoolHomePage(page, size);
    }

    @PostMapping("/createSchool")
    public ApiResponse<School> createSchool(@RequestBody SchoolCreateBody schoolCreateBody) {
        return schoolService.createSchool(schoolCreateBody);
    }

    @GetMapping("/getSchoolById/{id}")
    public ApiResponse<SchoolPageDto> getSchoolById(@PathVariable("id") UUID id) {
        return schoolService.getSchoolById(id);
    }

    @PostMapping("{id}/uploadCoverImage")
    public ApiResponse<String> uploadCoverImage(@RequestParam("file") MultipartFile file, @PathVariable("id") UUID id) {
        return schoolImageServices.uploadCoverImage(file, id);
    }

    @PostMapping("{id}/uploadSchoolImages")
    public ApiResponse<String> uploadSchoolImages(@RequestParam("files") MultipartFile[] files,
            @PathVariable("id") UUID id) {
        return schoolImageServices.uploadSchoolImages(files, id);
    }
}
