package org.swe304.schoolmanagement2.controller;

import org.swe304.schoolmanagement2.domain.dto.req.EnrollmentCreateRequest;
import org.swe304.schoolmanagement2.domain.dto.res.EnrollmentResponse;
import org.swe304.schoolmanagement2.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@Tag(name = "Enrollment", description = "Enrollment management APIs")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Operation(summary = "Create a new enrollment", description = "Enrolls a student in a course.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Enrollment created successfully", content = {@Content(schema = @Schema(implementation = EnrollmentResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid input data or student/course not found", content = {@Content(schema = @Schema())})
    })
    @PostMapping("/create")
    public ResponseEntity<EnrollmentResponse> createEnrollment(@Valid @RequestBody EnrollmentCreateRequest request) {
        return new ResponseEntity<>(enrollmentService.createEnrollment(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete an enrollment", description = "Soft deletes an enrollment by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Enrollment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found", content = {@Content(schema = @Schema())})
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get an enrollment by ID", description = "Retrieves an enrollment's details by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Enrollment found", content = {@Content(schema = @Schema(implementation = EnrollmentResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Enrollment not found", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<EnrollmentResponse> getEnrollmentById(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(id));
    }

    @Operation(summary = "Get all enrollments", description = "Retrieves a list of all active (not deleted) enrollments.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of enrollments retrieved successfully", content = {@Content(schema = @Schema(implementation = EnrollmentResponse.class), mediaType = "application/json")})
    })
    @GetMapping("/get-all")
    public ResponseEntity<List<EnrollmentResponse>> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }
}
