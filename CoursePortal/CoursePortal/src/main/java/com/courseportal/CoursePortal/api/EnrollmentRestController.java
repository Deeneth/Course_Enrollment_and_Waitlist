package com.courseportal.CoursePortal.api;

import com.courseportal.CoursePortal.model.Enrollment;
import com.courseportal.CoursePortal.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EnrollmentRestController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<?> enrollStudent(@RequestBody Map<String, Long> request) {
        try {
            Long studentId = request.get("studentId");
            Long courseId = request.get("courseId");
            Enrollment enrollment = enrollmentService.enrollStudentByIds(studentId, courseId);
            return new ResponseEntity<>(enrollment, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        try {
            return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudent(studentId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        try {
            return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(courseId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/update-status")
    public ResponseEntity<?> updateEnrollmentStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            Enrollment enrollment = enrollmentService.getEnrollmentById(id)
                    .orElseThrow(() -> new RuntimeException("Enrollment not found"));

            Long studentId = enrollment.getStudent().getStudentId();
            Long courseId = enrollment.getCourse().getCourseId();

            Enrollment updated = enrollmentService.updateEnrollment(id, studentId, courseId, status);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        try {
            enrollmentService.deleteEnrollment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/waitlist/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getWaitlistForCourse(@PathVariable Long courseId) {
        try {
            return ResponseEntity.ok(enrollmentService.getWaitlistForCourse(courseId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
