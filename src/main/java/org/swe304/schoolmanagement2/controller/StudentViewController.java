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
import org.swe304.schoolmanagement2.domain.dto.req.StudentCreateRequest;
import org.swe304.schoolmanagement2.domain.dto.req.StudentUpdateRequest;
import org.swe304.schoolmanagement2.domain.dto.res.StudentResponse;
import org.swe304.schoolmanagement2.exception.DuplicateResourceException;
import org.swe304.schoolmanagement2.exception.ResourceNotFoundException;
import org.swe304.schoolmanagement2.service.StudentService;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentViewController {

    private final StudentService studentService;

    @GetMapping
    public String students(Model model) {
        addPageData(model, new StudentCreateRequest(), false, null);
        return "students";
    }

    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            StudentResponse student = studentService.getStudentById(id);
            StudentUpdateRequest request = new StudentUpdateRequest();
            request.setFirstName(student.getFirstName());
            request.setLastName(student.getLastName());
            request.setEmail(student.getEmail());
            addPageData(model, request, true, id);
            return "students";
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/students";
        }
    }

    @PostMapping
    public String createStudent(
            @Valid @ModelAttribute("studentForm") StudentCreateRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            addPageData(model, request, false, null);
            return "students";
        }

        try {
            studentService.createStudent(request);
            redirectAttributes.addFlashAttribute("successMessage", "Ogrenci kaydedildi.");
            return "redirect:/students";
        } catch (DuplicateResourceException ex) {
            bindingResult.reject("student.duplicate", ex.getMessage());
            addPageData(model, request, false, null);
            return "students";
        }
    }

    @PostMapping("/{id}")
    public String updateStudent(
            @PathVariable Long id,
            @Valid @ModelAttribute("studentForm") StudentUpdateRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            addPageData(model, request, true, id);
            return "students";
        }

        try {
            studentService.updateStudent(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Ogrenci guncellendi.");
            return "redirect:/students";
        } catch (DuplicateResourceException | ResourceNotFoundException ex) {
            bindingResult.reject("student.save", ex.getMessage());
            addPageData(model, request, true, id);
            return "students";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("successMessage", "Ogrenci silindi.");
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/students";
    }

    private void addPageData(Model model, Object form, boolean editing, Long editingId) {
        model.addAttribute("activePage", "students");
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("studentForm", form);
        model.addAttribute("editing", editing);
        model.addAttribute("editingId", editingId);
    }
}
