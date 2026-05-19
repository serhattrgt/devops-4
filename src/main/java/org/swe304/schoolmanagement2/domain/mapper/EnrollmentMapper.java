package org.swe304.schoolmanagement2.domain.mapper;

import org.swe304.schoolmanagement2.domain.dto.res.EnrollmentResponse;
import org.swe304.schoolmanagement2.domain.entity.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "course.id", target = "courseId")
    EnrollmentResponse toResponse(Enrollment enrollment);
}
