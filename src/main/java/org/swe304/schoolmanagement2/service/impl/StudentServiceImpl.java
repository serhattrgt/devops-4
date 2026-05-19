package org.swe304.schoolmanagement2.service.impl;

import org.swe304.schoolmanagement2.domain.dto.req.StudentCreateRequest;
import org.swe304.schoolmanagement2.domain.dto.req.StudentUpdateRequest;
import org.swe304.schoolmanagement2.domain.dto.res.StudentResponse;
import org.swe304.schoolmanagement2.domain.entity.Student;
import org.swe304.schoolmanagement2.domain.mapper.StudentMapper;
import org.swe304.schoolmanagement2.exception.DuplicateResourceException;
import org.swe304.schoolmanagement2.exception.ResourceNotFoundException;
import org.swe304.schoolmanagement2.repository.StudentRepository;
import org.swe304.schoolmanagement2.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    @Transactional
    public StudentResponse createStudent(StudentCreateRequest request) {
        Optional<Student> existingStudent = studentRepository.findByEmail(request.getEmail());
        
        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();
            if (student.isDeleted()) {
                // Reactivate the student
                student.setDeleted(false);
                student.setFirstName(request.getFirstName());
                student.setLastName(request.getLastName());
                Student savedStudent = studentRepository.save(student);
                return studentMapper.toResponse(savedStudent);
            } else {
                throw new DuplicateResourceException("Student with email " + request.getEmail() + " already exists.");
            }
        }

        Student student = studentMapper.toEntity(request);
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toResponse(savedStudent);
    }

    @Override
    @Transactional
    public StudentResponse updateStudent(Long id, StudentUpdateRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        
        if (student.isDeleted()) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }

        // Check if email is being updated and if it already exists for another student
        if (request.getEmail() != null && !request.getEmail().equals(student.getEmail())) {
            Optional<Student> studentWithEmail = studentRepository.findByEmail(request.getEmail());
            if (studentWithEmail.isPresent()) {
                throw new DuplicateResourceException("Student with email " + request.getEmail() + " already exists.");
            }
        }

        studentMapper.updateEntityFromRequest(request, student);
        
        Student updatedStudent = studentRepository.save(student);
        return studentMapper.toResponse(updatedStudent);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        
        if (student.isDeleted()) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }

        student.setDeleted(true);
        studentRepository.save(student);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        
        if (student.isDeleted()) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }

        return studentMapper.toResponse(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .filter(student -> !student.isDeleted())
                .map(studentMapper::toResponse)
                .collect(Collectors.toList());
    }
}
