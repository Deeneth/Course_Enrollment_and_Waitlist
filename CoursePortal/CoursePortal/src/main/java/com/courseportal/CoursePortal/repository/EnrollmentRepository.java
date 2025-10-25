package com.courseportal.CoursePortal.repository;

import com.courseportal.CoursePortal.model.Enrollment;
import com.courseportal.CoursePortal.model.Course;
import com.courseportal.CoursePortal.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // Get all enrollments of a specific student
    List<Enrollment> findByStudent(Student student);

    // Get all enrollments of a specific course
    List<Enrollment> findByCourse(Course course);

    // Optional: get enrollment of a student in a specific course
    Optional<Enrollment> findByStudentAndCourse(Student student, Course course);

    // Get all waitlisted enrollments for a course
    List<Enrollment> findByCourseAndStatus(Course course, String status);
}
