package com.courseportal.CoursePortal.service;

import com.courseportal.CoursePortal.model.Enrollment;
import com.courseportal.CoursePortal.model.Student;
import com.courseportal.CoursePortal.model.Course;
import com.courseportal.CoursePortal.repository.EnrollmentRepository;
import com.courseportal.CoursePortal.repository.StudentRepository;
import com.courseportal.CoursePortal.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public Enrollment enrollStudentByIds(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Optional<Enrollment> existing = enrollmentRepository.findByStudentAndCourse(student, course);
        if (existing.isPresent()) {
            throw new RuntimeException("Student already enrolled in this course");
        }

        long enrolledCount = enrollmentRepository.findByCourseAndStatus(course, "ENROLLED").size();
        String status = (enrolledCount < course.getMaxCapacity()) ? "ENROLLED" : "WAITLISTED";

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .status(status)
                .enrolledOn(LocalDateTime.now())
                .build();

        return enrollmentRepository.save(enrollment);
    }

    public void promoteFromWaitlist(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        long enrolledCount = enrollmentRepository.findByCourseAndStatus(course, "ENROLLED").size();

        if (enrolledCount < course.getMaxCapacity()) {
            List<Enrollment> waitlist = enrollmentRepository.findByCourseAndStatus(course, "WAITLISTED");

            int spotsAvailable = (int) (course.getMaxCapacity() - enrolledCount);
            int toPromote = Math.min(spotsAvailable, waitlist.size());

            for (int i = 0; i < toPromote; i++) {
                Enrollment enrollment = waitlist.get(i);
                enrollment.setStatus("ENROLLED");
                enrollmentRepository.save(enrollment);
            }
        }
    }

    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return enrollmentRepository.findByStudent(student);
    }

    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return enrollmentRepository.findByCourse(course);
    }

    // 🟢 Get all enrollments
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    // 🟢 Get enrollment by ID
    public Optional<Enrollment> getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id);
    }

    // 🟢 Update enrollment
    public Enrollment updateEnrollment(Long enrollmentId, Long studentId, Long courseId, String status) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus(status);

        return enrollmentRepository.save(enrollment);
    }

    public void deleteEnrollment(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        Long courseId = enrollment.getCourse().getCourseId();
        enrollmentRepository.deleteById(id);
        promoteFromWaitlist(courseId);
    }

    // 🟢 Get waitlisted students for a course
    public List<Enrollment> getWaitlistForCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return enrollmentRepository.findByCourseAndStatus(course, "WAITLISTED");
    }
}
