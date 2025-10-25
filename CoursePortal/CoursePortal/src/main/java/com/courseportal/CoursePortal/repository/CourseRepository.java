package com.courseportal.CoursePortal.repository;

import com.courseportal.CoursePortal.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Optional: find course by name
    boolean existsByName(String name);
}
