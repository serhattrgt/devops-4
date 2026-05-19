package org.swe304.schoolmanagement2.service;

import org.swe304.schoolmanagement2.domain.dto.req.CourseCreateRequest;
import org.swe304.schoolmanagement2.domain.dto.req.CourseUpdateRequest;
import org.swe304.schoolmanagement2.domain.dto.res.CourseResponse;

import java.util.List;

public interface CourseService {
    CourseResponse createCourse(CourseCreateRequest request);
    CourseResponse updateCourse(Long id, CourseUpdateRequest request);
    void deleteCourse(Long id);
    CourseResponse getCourseById(Long id);
    List<CourseResponse> getAllCourses();
}
