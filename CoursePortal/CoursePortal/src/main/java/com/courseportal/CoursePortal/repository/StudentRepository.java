package com.courseportal.CoursePortal.repository;

import com.courseportal.CoursePortal.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Optional: find student by email
    Optional<Student> findByEmail(String email);
}
