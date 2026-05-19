package org.swe304.schoolmanagement2.controller;

import org.swe304.schoolmanagement2.domain.dto.req.CourseCreateRequest;
import org.swe304.schoolmanagement2.domain.dto.req.CourseUpdateRequest;
import org.swe304.schoolmanagement2.domain.dto.res.CourseResponse;
import org.swe304.schoolmanagement2.service.CourseService;
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
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "Course", description = "Course management APIs")
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Create a new course", description = "Creates a new course with the provided details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Course created successfully", content = {@Content(schema = @Schema(implementation = CourseResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = {@Content(schema = @Schema())})
    })
    @PostMapping("/create")
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseCreateRequest request) {
        return new ResponseEntity<>(courseService.createCourse(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing course", description = "Updates the details of an existing course by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course updated successfully", content = {@Content(schema = @Schema(implementation = CourseResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Course not found", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = {@Content(schema = @Schema())})
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseUpdateRequest request) {
        return ResponseEntity.ok(courseService.updateCourse(id, request));
    }

    @Operation(summary = "Delete a course", description = "Soft deletes a course by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Course deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found", content = {@Content(schema = @Schema())})
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a course by ID", description = "Retrieves a course's details by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course found", content = {@Content(schema = @Schema(implementation = CourseResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Course not found", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @Operation(summary = "Get all courses", description = "Retrieves a list of all active (not deleted) courses.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of courses retrieved successfully", content = {@Content(schema = @Schema(implementation = CourseResponse.class), mediaType = "application/json")})
    })
    @GetMapping("/get-all")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }
}
