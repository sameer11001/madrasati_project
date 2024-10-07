package com.webapp.madrasati.school.controller;

import com.webapp.madrasati.core.model.ApiResponseBody;
import com.webapp.madrasati.school.service.SchoolImageService;
import com.webapp.madrasati.school.service.imp.SchoolImageUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RequestMapping("/schoolTest/v1/")
@RestController
@AllArgsConstructor
public class SchoolTestController {

    SchoolImageUploadService schoolImageServices;

    @PostMapping(value = "{id}/uploadCoverImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload cover image", description = "Uploads a cover image for a school")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cover image uploaded successfully", content = @Content(schema = @Schema(implementation = ApiResponseBody.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "School not found")
    })
    public ApiResponseBody<String> uploadCoverImage(
            @Parameter(description = "Image file", required = true) @RequestPart("file") MultipartFile file,
            @Parameter(description = "School ID", required = true) @PathVariable("id") String schoolId)
            throws InterruptedException, ExecutionException {
        return ApiResponseBody.success(schoolImageServices.uploadCoverImage(file,
                        schoolId),
                "Cover image uploaded successfully", HttpStatus.CREATED);
    }

    @PostMapping(value = "{id}/uploadSchoolImages", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload school images", description = "Uploads multiple images for a school")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "School images uploaded successfully", content = @Content(schema = @Schema(implementation = ApiResponseBody.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "School not found")
    })
    public ApiResponseBody<List<String>> uploadSchoolImages(
            @Parameter(description = "Image files", required = true) @RequestPart("files") MultipartFile[] files,
            @Parameter(description = "School ID", required = true) @PathVariable("id") String schoolId)
            throws InterruptedException, ExecutionException {
        return ApiResponseBody.success(schoolImageServices.uploadSchoolImages(files,
                        schoolId),
                "School images uploaded successfully", HttpStatus.CREATED);

    }

}
