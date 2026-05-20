package org.swe304.schoolmanagement2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.swe304.schoolmanagement2.service.CourseService;
import org.swe304.schoolmanagement2.service.EnrollmentService;
import org.swe304.schoolmanagement2.service.StudentService;

@Controller
@RequiredArgsConstructor
public class HomeViewController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("activePage", "home");
        model.addAttribute("studentCount", studentService.getAllStudents().size());
        model.addAttribute("courseCount", courseService.getAllCourses().size());
        model.addAttribute("enrollmentCount", enrollmentService.getAllEnrollments().size());
        return "home";
    }
}
