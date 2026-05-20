package org.swe304.schoolmanagement2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.swe304.schoolmanagement2.domain.dto.req.CourseCreateRequest;
import org.swe304.schoolmanagement2.domain.dto.req.CourseUpdateRequest;
import org.swe304.schoolmanagement2.domain.dto.res.CourseResponse;
import org.swe304.schoolmanagement2.exception.ResourceNotFoundException;
import org.swe304.schoolmanagement2.service.CourseService;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseViewController {

    private final CourseService courseService;

    @GetMapping
    public String courses(Model model) {
        addPageData(model, new CourseCreateRequest(), false, null);
        return "courses";
    }

    @GetMapping("/edit/{id}")
    public String editCourse(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            CourseResponse course = courseService.getCourseById(id);
            CourseUpdateRequest request = new CourseUpdateRequest();
            request.setCourseName(course.getCourseName());
            request.setCourseCode(course.getCourseCode());
            request.setCredit(course.getCredit());
            addPageData(model, request, true, id);
            return "courses";
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/courses";
        }
    }

    @PostMapping
    public String createCourse(
            @Valid @ModelAttribute("courseForm") CourseCreateRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            addPageData(model, request, false, null);
            return "courses";
        }

        try {
            courseService.createCourse(request);
            redirectAttributes.addFlashAttribute("successMessage", "Ders kaydedildi.");
            return "redirect:/courses";
        } catch (DataIntegrityViolationException ex) {
            bindingResult.reject("course.duplicate", "Bu ders kodu zaten kullaniliyor.");
            addPageData(model, request, false, null);
            return "courses";
        }
    }

    @PostMapping("/{id}")
    public String updateCourse(
            @PathVariable Long id,
            @Valid @ModelAttribute("courseForm") CourseUpdateRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            addPageData(model, request, true, id);
            return "courses";
        }

        try {
            courseService.updateCourse(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Ders guncellendi.");
            return "redirect:/courses";
        } catch (DataIntegrityViolationException ex) {
            bindingResult.reject("course.duplicate", "Bu ders kodu zaten kullaniliyor.");
            addPageData(model, request, true, id);
            return "courses";
        } catch (ResourceNotFoundException ex) {
            bindingResult.reject("course.save", ex.getMessage());
            addPageData(model, request, true, id);
            return "courses";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            courseService.deleteCourse(id);
            redirectAttributes.addFlashAttribute("successMessage", "Ders silindi.");
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/courses";
    }

    private void addPageData(Model model, Object form, boolean editing, Long editingId) {
        model.addAttribute("activePage", "courses");
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("courseForm", form);
        model.addAttribute("editing", editing);
        model.addAttribute("editingId", editingId);
    }
}
