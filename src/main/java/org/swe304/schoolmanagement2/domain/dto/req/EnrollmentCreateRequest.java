package org.swe304.schoolmanagement2.domain.dto.req;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentCreateRequest {

    @NotNull(message = "Student id cannot be null")
    private Long studentId;

    @NotNull(message = "Course id cannot be null")
    private Long courseId;
}
