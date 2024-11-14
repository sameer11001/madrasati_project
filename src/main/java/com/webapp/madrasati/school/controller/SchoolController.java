package com.webapp.madrasati.school.controller;

import com.webapp.madrasati.school.model.SchoolFeedBack;
import com.webapp.madrasati.school.model.SchoolRating;
import com.webapp.madrasati.school.model.dto.req.SchoolEditBodyDto;
import com.webapp.madrasati.school.model.dto.req.SchoolFeedBackDto;
import com.webapp.madrasati.school.model.dto.res.CreateNewSchoolDto;
import com.webapp.madrasati.school.model.dto.res.SchoolEditResponseDto;
import com.webapp.madrasati.school.repository.summary.SchoolFeedBackSummary;
import com.webapp.madrasati.school.service.SchoolImageService;
import com.webapp.madrasati.school.service.SchoolService;
import com.webapp.madrasati.school.service.imp.SchoolFeatureServicesImp;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.madrasati.core.model.ApiResponseBody;
import com.webapp.madrasati.school.model.dto.SchoolDto;
import com.webapp.madrasati.school.model.dto.req.SchoolCreateBody;
import com.webapp.madrasati.school.model.dto.res.SchoolProfilePageDto;
import com.webapp.madrasati.school.repository.summary.SchoolSummary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("v1/school")
@AllArgsConstructor
@Tag(name = "School", description = "Endpoints for managing schools")
public class SchoolController {

    private final SchoolService schoolService;

    private final SchoolImageService schoolImageServices;

    private final SchoolFeatureServicesImp schoolFeatureServicesImp;

    @GetMapping("/getAllSchools")
    @Operation(summary = "Get all schools", description = "Retrieves a paginated list of school summaries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved schools", content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseBody<Page<SchoolSummary>> getAllSchools(
            @Parameter(description = "Page number", schema = @Schema(type = "integer", defaultValue = "0")) @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "Page size", schema = @Schema(type = "integer", defaultValue = "1")) @RequestParam(name = "size", defaultValue = "1") int size) {

        return ApiResponseBody.success(schoolService.getSchoolHomePage(page, size));
    }

    @PostMapping("/createSchool")
    @Operation(summary = "Create a school", description = "Creates a new school")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "School created successfully", content = @Content(schema = @Schema(implementation = SchoolDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseBody<CreateNewSchoolDto> createSchool(@RequestBody SchoolCreateBody body) {
        return ApiResponseBody.success(schoolService.createSchool(body),
                "School created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/getSchoolById/{id}")
    @Operation(summary = "Get school by ID", description = "Retrieves a school's details by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved school", content = @Content(schema = @Schema(implementation = SchoolProfilePageDto.class))),
            @ApiResponse(responseCode = "404", description = "School not found")
    })
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseBody<SchoolProfilePageDto> getSchoolById(@PathVariable("id") @NotEmpty String schoolId) {
        return ApiResponseBody.success(schoolService.fetchSchoolById(schoolId), "School retrieved successfully",
                HttpStatus.OK);
    }

    @PutMapping("/updateSchool")
    @Operation(summary = "Update school", description = "Updates a school's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated school", content = @Content(schema = @Schema(implementation = SchoolEditResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "School not found")
    })
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseBody<SchoolEditResponseDto> updateSchool(@RequestBody SchoolEditBodyDto body,@RequestParam("schoolId") @NotEmpty String schoolId) {
        return ApiResponseBody.success(schoolService.editSchoolInfo(body,schoolId), "School updated successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}/getSchoolFeedBacks")
    @Operation(summary = "Get school feed back", description = "Retrieves a school's feed back by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved school feed back", content = @Content(schema = @Schema(implementation = SchoolFeedBackSummary.class))),
            @ApiResponse(responseCode = "400", description = "invalid page or size"),
            @ApiResponse(responseCode = "404", description = "School not found")
    })
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseBody<Page<SchoolFeedBackSummary>> getSchoolFeedBacks(@Parameter(description = "School ID", required = true) @PathVariable("id") @NotEmpty String schoolId,
                                                                           @Parameter(description = "Page number", schema = @Schema(type = "integer", defaultValue = "0")) @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "1") int size) {
        return ApiResponseBody.success(schoolFeatureServicesImp.getSchoolFeedBack(schoolId, page, size));
    }

    @PostMapping("/{id}/addFeedBack")
    @Operation(summary = "Add school feed back", description = "Adds a school feed back")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "School feed back added successfully", content = @Content(schema = @Schema(implementation = ApiResponseBody.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "School not found")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseBody<SchoolFeedBack> addFeedBack(@Parameter(description = "School ID", required = true) @PathVariable("id") @NotEmpty String schoolIdString, @RequestBody SchoolFeedBackDto body) {
        return ApiResponseBody.success(schoolFeatureServicesImp.addFeedBack(body.getFeedBack(), schoolIdString),"School feed back added successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("deleteFeedBack/{feedBackId}")
    @Operation(summary = "Delete school feed back", description = "Deletes a school feed back")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "School feed back deleted successfully", content = @Content(schema = @Schema(implementation = ApiResponseBody.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "School not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponseBody<Void> deleteFeedBack(@Parameter(description = "Feed back ID", required = true) @PathVariable("feedBackId") @NotEmpty String feedBackId) {
        schoolFeatureServicesImp.deleteFeedBack(feedBackId);
        return ApiResponseBody.successWithNoData;
    }

    @PostMapping("/{id}/rateSchool")
    @Operation(summary = "Rate school", description = "Rates a school")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "School rating added successfully", content = @Content(schema = @Schema(implementation = ApiResponseBody.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "School not found")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseBody<SchoolRating> rateSchool(@Parameter(description = "School ID", required = true) @PathVariable("id") @NotEmpty String schoolIdString, @Parameter(description = "Rate number", required = true, example = "1") @RequestParam("rateNumber") Integer rateNumber) {
        return ApiResponseBody.success(schoolFeatureServicesImp.rateSchool(rateNumber, schoolIdString), "School rating added successfully", HttpStatus.CREATED);
    }

    @PostMapping(value = "/{id}/uploadCoverImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload cover image", description = "Uploads a cover image for a school")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cover image uploaded successfully", content = @Content(schema = @Schema(implementation = ApiResponseBody.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "School not found")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseBody<String> uploadCoverImage(
            @Parameter(description = "Image file", required = true) @Valid @NotNull @RequestPart("file") MultipartFile file,
            @Parameter(description = "School ID", required = true) @NotEmpty @PathVariable("id") String schoolId)
            throws InterruptedException, ExecutionException {
        return ApiResponseBody.success(schoolImageServices.uploadCoverImage(file,
                        schoolId).get(),
                "Cover image uploaded successfully", HttpStatus.CREATED);
    }

    @PostMapping(value = "/{id}/uploadSchoolImages", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload school images", description = "Uploads multiple images for a school")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "School images uploaded successfully", content = @Content(schema = @Schema(implementation = ApiResponseBody.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "School not found")
    })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResponseBody<List<String>> uploadSchoolImages(
            @Parameter(description = "Image files", required = true) @Valid @NotNull @RequestPart("files") List<MultipartFile> files,
            @Parameter(description = "School ID", required = true) @NotEmpty @PathVariable("id") String schoolId)
            throws InterruptedException, ExecutionException {
        return ApiResponseBody.success(schoolImageServices.uploadSchoolImages(files,
                        schoolId).get(),
                "School images uploaded successfully", HttpStatus.CREATED);

    }
}
