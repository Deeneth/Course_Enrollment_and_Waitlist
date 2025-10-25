package com.courseportal.CoursePortal.service;

import com.courseportal.CoursePortal.model.Course;
import com.courseportal.CoursePortal.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    // 🟢 Create new course
    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    // 🟢 Get all courses
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // 🟢 Find course by ID
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    // 🟢 Update course
   public void updateCourse(Long id, Course updatedCourse) {
    Course existing = courseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course not found"));
    existing.setName(updatedCourse.getName());
    existing.setDescription(updatedCourse.getDescription());
    existing.setMaxCapacity(updatedCourse.getMaxCapacity());
    courseRepository.save(existing);
}


    // 🟢 Delete course
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found");
        }
        courseRepository.deleteById(id);
    }
}
