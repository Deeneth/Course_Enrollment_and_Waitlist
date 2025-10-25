-- Course Portal Database Setup Script
-- Run this script to create the database and sample data

-- Create Database
CREATE DATABASE IF NOT EXISTS course_portal_db;
USE course_portal_db;

-- Note: Tables will be auto-created by Spring Boot (spring.jpa.hibernate.ddl-auto=update)
-- This script provides sample data for testing

-- Sample Students
INSERT INTO students (name, email, contact_no, age, department, join_date) VALUES
('John Doe', 'john.doe@example.com', '1234567890', 20, 'Computer Science', NOW()),
('Jane Smith', 'jane.smith@example.com', '2345678901', 21, 'Engineering', NOW()),
('Alice Johnson', 'alice.johnson@example.com', '3456789012', 19, 'Mathematics', NOW()),
('Bob Williams', 'bob.williams@example.com', '4567890123', 22, 'Physics', NOW()),
('Carol Davis', 'carol.davis@example.com', '5678901234', 20, 'Computer Science', NOW()),
('David Brown', 'david.brown@example.com', '6789012345', 21, 'Engineering', NOW()),
('Emma Wilson', 'emma.wilson@example.com', '7890123456', 19, 'Chemistry', NOW()),
('Frank Miller', 'frank.miller@example.com', '8901234567', 23, 'Biology', NOW());

-- Sample Courses
INSERT INTO courses (name, description, max_capacity, duration, created_at) VALUES
('Introduction to Java', 'Learn the fundamentals of Java programming including OOP concepts', 5, '8 weeks', NOW()),
('Data Structures & Algorithms', 'Comprehensive study of common data structures and algorithms', 4, '10 weeks', NOW()),
('Web Development with Spring Boot', 'Build modern web applications using Spring Boot framework', 6, '12 weeks', NOW()),
('Database Management Systems', 'Understanding relational databases, SQL, and database design', 5, '8 weeks', NOW()),
('Machine Learning Basics', 'Introduction to machine learning algorithms and applications', 4, '10 weeks', NOW());

-- Sample Enrollments (some enrolled, some waitlisted)
-- Course 1: Introduction to Java (capacity: 5)
INSERT INTO enrollments (student_id, course_id, enrolled_on, status) VALUES
(1, 1, NOW(), 'ENROLLED'),
(2, 1, NOW(), 'ENROLLED'),
(3, 1, NOW(), 'ENROLLED'),
(4, 1, NOW(), 'ENROLLED'),
(5, 1, NOW(), 'ENROLLED'),
(6, 1, NOW(), 'WAITLISTED'),
(7, 1, NOW(), 'WAITLISTED');

-- Course 2: Data Structures (capacity: 4)
INSERT INTO enrollments (student_id, course_id, enrolled_on, status) VALUES
(1, 2, NOW(), 'ENROLLED'),
(3, 2, NOW(), 'ENROLLED'),
(5, 2, NOW(), 'ENROLLED');

-- Course 3: Spring Boot (capacity: 6)
INSERT INTO enrollments (student_id, course_id, enrolled_on, status) VALUES
(2, 3, NOW(), 'ENROLLED'),
(4, 3, NOW(), 'ENROLLED'),
(6, 3, NOW(), 'ENROLLED'),
(8, 3, NOW(), 'ENROLLED');

-- Verification Queries
SELECT 'Students:' as Table_Name, COUNT(*) as Count FROM students
UNION ALL
SELECT 'Courses:' as Table_Name, COUNT(*) as Count FROM courses
UNION ALL
SELECT 'Enrollments:' as Table_Name, COUNT(*) as Count FROM enrollments;

-- Show Enrolled vs Waitlisted
SELECT
    c.name as Course_Name,
    c.max_capacity as Max_Capacity,
    COUNT(CASE WHEN e.status = 'ENROLLED' THEN 1 END) as Enrolled_Count,
    COUNT(CASE WHEN e.status = 'WAITLISTED' THEN 1 END) as Waitlisted_Count
FROM courses c
LEFT JOIN enrollments e ON c.course_id = e.course_id
GROUP BY c.course_id, c.name, c.max_capacity;
