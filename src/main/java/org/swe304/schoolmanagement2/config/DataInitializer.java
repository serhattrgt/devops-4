package org.swe304.schoolmanagement2.config;

import com.github.javafaker.Faker;
import org.swe304.schoolmanagement2.domain.entity.Course;
import org.swe304.schoolmanagement2.domain.entity.Enrollment;
import org.swe304.schoolmanagement2.domain.entity.Student;
import org.swe304.schoolmanagement2.repository.CourseRepository;
import org.swe304.schoolmanagement2.repository.EnrollmentRepository;
import org.swe304.schoolmanagement2.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    private final Faker faker = new Faker();
    private final Random random = new Random();

    @Override
    public void run(String... args) {

        if (studentRepository.count() > 0 || courseRepository.count() > 0) {
            log.info("Dummy data already exists. Skipping initialization...");
            return;
        }

        log.info("Database is empty. Inserting dummy data...");

        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Student student = new Student();
            student.setFirstName(faker.name().firstName());
            student.setLastName(faker.name().lastName());
            student.setEmail(faker.internet().emailAddress());
            student.setDeleted(false);
            students.add(student);
        }
        studentRepository.saveAll(students);
        log.info("Dummy students inserted: {}", students.size());

        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Course course = new Course();
            course.setCourseName("Course " + (i + 1));
            course.setCourseCode("CSE" + (100 + i));
            course.setCredit(random.nextInt(3) + 2); // 2-4 kredi
            course.setDeleted(false);
            courses.add(course);
        }
        courseRepository.saveAll(courses);
        log.info("Dummy courses inserted: {}", courses.size());

        List<Enrollment> enrollments = new ArrayList<>();

        for (Student student : students) {
            int courseCount = random.nextInt(3) + 1;

            java.util.Set<Long> pickedCourseIds = new java.util.HashSet<>();

            while (pickedCourseIds.size() < courseCount) {
                Course randomCourse = courses.get(random.nextInt(courses.size()));
                pickedCourseIds.add(randomCourse.getId());
            }

            for (Long courseId : pickedCourseIds) {
                Course course = courses.stream()
                        .filter(c -> c.getId().equals(courseId))
                        .findFirst()
                        .orElseThrow();

                Enrollment enrollment = new Enrollment();
                enrollment.setStudent(student);
                enrollment.setCourse(course);
                enrollment.setDeleted(false);
                enrollments.add(enrollment);
            }
        }

        enrollmentRepository.saveAll(enrollments);
        log.info("Dummy enrollments inserted: {}", enrollments.size());

        log.info("Dummy data initialization completed successfully");
    }
}