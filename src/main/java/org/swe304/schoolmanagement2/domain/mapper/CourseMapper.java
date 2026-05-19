package org.swe304.schoolmanagement2.domain.mapper;

import org.swe304.schoolmanagement2.domain.dto.req.CourseCreateRequest;
import org.swe304.schoolmanagement2.domain.dto.req.CourseUpdateRequest;
import org.swe304.schoolmanagement2.domain.dto.res.CourseResponse;
import org.swe304.schoolmanagement2.domain.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    Course toEntity(CourseCreateRequest request);
    CourseResponse toResponse(Course course);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    void updateEntityFromRequest(CourseUpdateRequest request, @MappingTarget Course course);
}
