package com.webapp.madrasati.school.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.madrasati.core.model.ApiResponseBody;
import com.webapp.madrasati.school.model.School;
import com.webapp.madrasati.school.model.dto.SchoolDto;
import com.webapp.madrasati.school.model.dto.req.SchoolCreateBody;
import com.webapp.madrasati.school.model.dto.res.SchoolPageDto;
import com.webapp.madrasati.school.repository.summary.SchoolSummary;
import com.webapp.madrasati.school.service.imp.SchoolImageServicesimp;
import com.webapp.madrasati.school.service.imp.SchoolServicesimp;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/school")
@AllArgsConstructor
@Tag(name = "School", description = "Endpoints for managing schools")
public class SchoolController {

        private SchoolServicesimp schoolService;

        private SchoolImageServicesimp schoolImageServices;

        @GetMapping("/getAllSchools")
        @Operation(summary = "Get all schools", description = "Retrieves a paginated list of school summaries")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved schools", content = @Content(schema = @Schema(implementation = Page.class)))
        })
        public ApiResponseBody<Page<SchoolSummary>> getAllSchools(
                        @Parameter(description = "Page number", schema = @Schema(type = "integer", defaultValue = "0")) @RequestParam(name = "page", defaultValue = "0") int page,
                        @Parameter(description = "Page size", schema = @Schema(type = "integer", defaultValue = "1")) @RequestParam(name = "size", defaultValue = "1") int size) {

                return ApiResponseBody.success(schoolService.getSchoolHomePage(page, size));
        }

        @PostMapping("/createSchool")
        @Operation(summary = "Create a school", description = "Creates a new school")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "School created successfully", content = @Content(schema = @Schema(implementation = SchoolDto.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input")
        })
        public ApiResponseBody<School> createSchool(@RequestBody SchoolCreateBody schoolCreateBody) {
                return ApiResponseBody.success(schoolService.createSchool(schoolCreateBody),
                                "School created successfully", HttpStatus.CREATED);
        }

        @GetMapping("/getSchoolById/{id}")
        @Operation(summary = "Get school by ID", description = "Retrieves a school's details by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved school", content = @Content(schema = @Schema(implementation = SchoolPageDto.class))),
                        @ApiResponse(responseCode = "404", description = "School not found")
        })
        public ApiResponseBody<SchoolPageDto> getSchoolById(@PathVariable("id") String schoolId) {
                return ApiResponseBody.success(schoolService.getSchoolById(schoolId), "School retrieved successfully",
                                HttpStatus.OK);
        }

        @PostMapping("{id}/uploadCoverImage")
        @Operation(summary = "Upload cover image", description = "Uploads a cover image for a school")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Cover image uploaded successfully", content = @Content(schema = @Schema(implementation = ApiResponseBody.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input"),
                        @ApiResponse(responseCode = "404", description = "School not found")
        })
        public ApiResponseBody<String> uploadCoverImage(
                        @Parameter(description = "Image file", required = true) @RequestParam("file") MultipartFile file,
                        @Parameter(description = "School ID", required = true) @PathVariable("id") UUID id) {
                return ApiResponseBody.success(schoolImageServices.uploadCoverImage(file, id),
                                "Cover image uploaded successfully", HttpStatus.CREATED);
        }

        @PostMapping("{id}/uploadSchoolImages")
        @Operation(summary = "Upload school images", description = "Uploads multiple images for a school")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "School images uploaded successfully", content = @Content(schema = @Schema(implementation = ApiResponseBody.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input"),
                        @ApiResponse(responseCode = "404", description = "School not found")
        })
        public ApiResponseBody<Void> uploadSchoolImages(
                        @Parameter(description = "Image files", required = true) @RequestParam("files") MultipartFile[] files,
                        @Parameter(description = "School ID", required = true) @PathVariable("id") UUID id) {
                schoolImageServices.uploadSchoolImages(files, id);
                return ApiResponseBody.successWithNoData;
        }
}
