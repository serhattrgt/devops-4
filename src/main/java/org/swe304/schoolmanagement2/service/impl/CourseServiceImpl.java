package org.swe304.schoolmanagement2.service.impl;

import org.swe304.schoolmanagement2.domain.dto.req.CourseCreateRequest;
import org.swe304.schoolmanagement2.domain.dto.req.CourseUpdateRequest;
import org.swe304.schoolmanagement2.domain.dto.res.CourseResponse;
import org.swe304.schoolmanagement2.domain.entity.Course;
import org.swe304.schoolmanagement2.domain.mapper.CourseMapper;
import org.swe304.schoolmanagement2.exception.ResourceNotFoundException;
import org.swe304.schoolmanagement2.repository.CourseRepository;
import org.swe304.schoolmanagement2.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    @Transactional
    public CourseResponse createCourse(CourseCreateRequest request) {
        Course course = courseMapper.toEntity(request);
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toResponse(savedCourse);
    }

    @Override
    @Transactional
    public CourseResponse updateCourse(Long id, CourseUpdateRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        
        if (course.isDeleted()) {
            throw new ResourceNotFoundException("Course not found with id: " + id);
        }

        courseMapper.updateEntityFromRequest(request, course);
        
        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toResponse(updatedCourse);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        
        if (course.isDeleted()) {
            throw new ResourceNotFoundException("Course not found with id: " + id);
        }

        course.setDeleted(true);
        courseRepository.save(course);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        
        if (course.isDeleted()) {
            throw new ResourceNotFoundException("Course not found with id: " + id);
        }

        return courseMapper.toResponse(course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .filter(course -> !course.isDeleted())
                .map(courseMapper::toResponse)
                .collect(Collectors.toList());
    }
}
