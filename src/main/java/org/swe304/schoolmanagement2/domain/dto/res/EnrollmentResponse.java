package org.swe304.schoolmanagement2.domain.dto.res;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentResponse {

    private Long id;
    private Long studentId;
    private Long courseId;
}
