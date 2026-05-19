package org.swe304.schoolmanagement2.controller;

import org.swe304.schoolmanagement2.domain.dto.req.StudentCreateRequest;
import org.swe304.schoolmanagement2.domain.dto.req.StudentUpdateRequest;
import org.swe304.schoolmanagement2.domain.dto.res.StudentResponse;
import org.swe304.schoolmanagement2.service.StudentService;
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
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Tag(name = "Student", description = "Student management APIs")
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "Create a new student", description = "Creates a new student with the provided details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Student created successfully", content = {@Content(schema = @Schema(implementation = StudentResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = {@Content(schema = @Schema())})
    })
    @PostMapping("/create")
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentCreateRequest request) {
        return new ResponseEntity<>(studentService.createStudent(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing student", description = "Updates the details of an existing student by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student updated successfully", content = {@Content(schema = @Schema(implementation = StudentResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Student not found", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = {@Content(schema = @Schema())})
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentUpdateRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    @Operation(summary = "Delete a student", description = "Soft deletes a student by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found", content = {@Content(schema = @Schema())})
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a student by ID", description = "Retrieves a student's details by their ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student found", content = {@Content(schema = @Schema(implementation = StudentResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Student not found", content = {@Content(schema = @Schema())})
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @Operation(summary = "Get all students", description = "Retrieves a list of all active (not deleted) students.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of students retrieved successfully", content = {@Content(schema = @Schema(implementation = StudentResponse.class), mediaType = "application/json")})
    })
    @GetMapping("/get-all")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }
}
