package org.swe304.schoolmanagement2.domain.dto.req;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseCreateRequest {

    @NotBlank(message = "Course name cannot be empty")
    private String courseName;

    @NotBlank(message = "Course code cannot be empty")
    private String courseCode;

    @NotNull(message = "Credit cannot be null")
    private Integer credit;
}