package org.swe304.schoolmanagement2.service.impl;

import org.swe304.schoolmanagement2.domain.dto.req.EnrollmentCreateRequest;
import org.swe304.schoolmanagement2.domain.dto.res.EnrollmentResponse;
import org.swe304.schoolmanagement2.domain.entity.Course;
import org.swe304.schoolmanagement2.domain.entity.Enrollment;
import org.swe304.schoolmanagement2.domain.entity.Student;
import org.swe304.schoolmanagement2.domain.mapper.EnrollmentMapper;
import org.swe304.schoolmanagement2.exception.DuplicateResourceException;
import org.swe304.schoolmanagement2.exception.ResourceNotFoundException;
import org.swe304.schoolmanagement2.repository.CourseRepository;
import org.swe304.schoolmanagement2.repository.EnrollmentRepository;
import org.swe304.schoolmanagement2.repository.StudentRepository;
import org.swe304.schoolmanagement2.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    @Transactional
    public EnrollmentResponse createEnrollment(EnrollmentCreateRequest request) {
        Optional<Enrollment> existingEnrollment = enrollmentRepository.findByStudentIdAndCourseId(request.getStudentId(), request.getCourseId());
        
        if (existingEnrollment.isPresent()) {
            Enrollment enrollment = existingEnrollment.get();
            if (enrollment.isDeleted()) {
                // Reactivate the enrollment
                enrollment.setDeleted(false);
                Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
                return enrollmentMapper.toResponse(savedEnrollment);
            } else {
                throw new DuplicateResourceException("Student is already enrolled in this course.");
            }
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + request.getStudentId()));
        
        if (student.isDeleted()) {
            throw new ResourceNotFoundException("Student not found with id: " + request.getStudentId());
        }

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));
        
        if (course.isDeleted()) {
            throw new ResourceNotFoundException("Course not found with id: " + request.getCourseId());
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toResponse(savedEnrollment);
    }

    @Override
    @Transactional
    public void deleteEnrollment(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
        
        if (enrollment.isDeleted()) {
            throw new ResourceNotFoundException("Enrollment not found with id: " + id);
        }

        enrollment.setDeleted(true);
        enrollmentRepository.save(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentResponse getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
        
        if (enrollment.isDeleted()) {
            throw new ResourceNotFoundException("Enrollment not found with id: " + id);
        }

        return enrollmentMapper.toResponse(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getAllEnrollments() {
        return enrollmentRepository.findAll().stream()
                .filter(enrollment -> !enrollment.isDeleted())
                .map(enrollmentMapper::toResponse)
                .collect(Collectors.toList());
    }
}
