package org.swe304.schoolmanagement2.service;

import org.swe304.schoolmanagement2.domain.dto.req.EnrollmentCreateRequest;
import org.swe304.schoolmanagement2.domain.dto.res.EnrollmentResponse;

import java.util.List;

public interface EnrollmentService {
    EnrollmentResponse createEnrollment(EnrollmentCreateRequest request);
    void deleteEnrollment(Long id);
    EnrollmentResponse getEnrollmentById(Long id);
    List<EnrollmentResponse> getAllEnrollments();
}
