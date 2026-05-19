package org.swe304.schoolmanagement2.domain.dto.res;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseResponse {

    private Long id;
    private String courseName;
    private String courseCode;
    private Integer credit;
}