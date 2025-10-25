package com.courseportal.CoursePortal.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String contactNo;

    @Column
    private Integer age;  // Added age field

    @Column(length = 50)
    private String department;  // Added department field

    @Builder.Default
    private LocalDateTime joinDate = LocalDateTime.now();
}
