package org.swe304.schoolmanagement2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.swe304.schoolmanagement2.domain.dto.req.EnrollmentCreateRequest;
import org.swe304.schoolmanagement2.exception.DuplicateResourceException;
import org.swe304.schoolmanagement2.exception.ResourceNotFoundException;
import org.swe304.schoolmanagement2.service.CourseService;
import org.swe304.schoolmanagement2.service.EnrollmentService;
import org.swe304.schoolmanagement2.service.StudentService;

@Controller
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentViewController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final CourseService courseService;

    @GetMapping
    public String enrollments(Model model) {
        addPageData(model, new EnrollmentCreateRequest());
        return "enrollments";
    }

    @PostMapping
    public String createEnrollment(
            @Valid @ModelAttribute("enrollmentForm") EnrollmentCreateRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            addPageData(model, request);
            return "enrollments";
        }

        try {
            enrollmentService.createEnrollment(request);
            redirectAttributes.addFlashAttribute("successMessage", "Kayit olusturuldu.");
            return "redirect:/enrollments";
        } catch (DuplicateResourceException | ResourceNotFoundException ex) {
            bindingResult.reject("enrollment.save", ex.getMessage());
            addPageData(model, request);
            return "enrollments";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteEnrollment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            enrollmentService.deleteEnrollment(id);
            redirectAttributes.addFlashAttribute("successMessage", "Kayit silindi.");
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/enrollments";
    }

    private void addPageData(Model model, EnrollmentCreateRequest form) {
        model.addAttribute("activePage", "enrollments");
        model.addAttribute("enrollments", enrollmentService.getAllEnrollments());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("enrollmentForm", form);
    }
}
