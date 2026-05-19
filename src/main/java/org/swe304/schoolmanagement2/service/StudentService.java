package org.swe304.schoolmanagement2.service;

import org.swe304.schoolmanagement2.domain.dto.req.StudentCreateRequest;
import org.swe304.schoolmanagement2.domain.dto.req.StudentUpdateRequest;
import org.swe304.schoolmanagement2.domain.dto.res.StudentResponse;

import java.util.List;

public interface StudentService {
    StudentResponse createStudent(StudentCreateRequest request);
    StudentResponse updateStudent(Long id, StudentUpdateRequest request);
    void deleteStudent(Long id);
    StudentResponse getStudentById(Long id);
    List<StudentResponse> getAllStudents();
}
