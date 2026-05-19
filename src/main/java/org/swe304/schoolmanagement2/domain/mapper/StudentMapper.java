package org.swe304.schoolmanagement2.domain.mapper;

import org.swe304.schoolmanagement2.domain.dto.req.StudentCreateRequest;
import org.swe304.schoolmanagement2.domain.dto.req.StudentUpdateRequest;
import org.swe304.schoolmanagement2.domain.dto.res.StudentResponse;
import org.swe304.schoolmanagement2.domain.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    Student toEntity(StudentCreateRequest request);
    StudentResponse toResponse(Student student);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    void updateEntityFromRequest(StudentUpdateRequest request, @MappingTarget Student student);
}
