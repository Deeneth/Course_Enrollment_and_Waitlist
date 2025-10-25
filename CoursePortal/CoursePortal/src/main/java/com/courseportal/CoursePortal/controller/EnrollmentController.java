package com.courseportal.CoursePortal.controller;

import com.courseportal.CoursePortal.model.Enrollment;
import com.courseportal.CoursePortal.service.EnrollmentService;
import com.courseportal.CoursePortal.service.StudentService;
import com.courseportal.CoursePortal.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final CourseService courseService;

    // Show all enrollments
    @GetMapping
    public String getAllEnrollments(Model model) {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        model.addAttribute("enrollments", enrollments);
        return "enrollments/view";
    }

    // Show form to add enrollment
    @GetMapping("/add")
    public String showAddEnrollmentForm(Model model) {
        model.addAttribute("enrollment", new Enrollment());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", courseService.getAllCourses());
        return "enrollments/add";
    }

    // Handle add enrollment
    @PostMapping("/add")
    public String addEnrollment(@RequestParam Long studentId,
                                @RequestParam Long courseId) {
        enrollmentService.enrollStudentByIds(studentId, courseId);
        return "redirect:/enrollments";
    }

    // Show form to edit enrollment
    @GetMapping("/edit/{id}")
    public String showEditEnrollmentForm(@PathVariable Long id, Model model) {
        Optional<Enrollment> enrollment = enrollmentService.getEnrollmentById(id);
        model.addAttribute("enrollment", enrollment);
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", courseService.getAllCourses());
        return "enrollments/edit";
    }

    // Handle update enrollment
    @PostMapping("/edit/{id}")
    public String updateEnrollment(@PathVariable Long id,
                                   @RequestParam Long studentId,
                                   @RequestParam Long courseId,
                                   @RequestParam String status) {
        enrollmentService.updateEnrollment(id, studentId, courseId, status);
        return "redirect:/enrollments";
    }

    // Delete enrollment
    @GetMapping("/delete/{id}")
    public String deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return "redirect:/enrollments";
    }

    // Show waitlist for a course
    @GetMapping("/waitlist/{courseId}")
    public String getWaitlistForCourse(@PathVariable Long courseId, Model model) {
        model.addAttribute("waitlist", enrollmentService.getWaitlistForCourse(courseId));
        model.addAttribute("courseId", courseId);
        return "enrollments/waitlist";
    }
}
