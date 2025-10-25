# Course Enrollment & Waitlist Management System

## Project Overview
A comprehensive Spring Boot web application for managing academic course enrollments, student registrations, and automated waitlist handling.

## Features
- **Student Management**: Add, edit, view, and delete student records
- **Course Management**: Create and manage courses with capacity limits
- **Smart Enrollment**: Automatic waitlist when courses reach capacity
- **Auto-Promotion**: Automatically moves students from waitlist to enrolled when spots open
- **REST API**: Full REST API support for programmatic access
- **Modern UI**: Responsive Bootstrap interface with intuitive navigation

## Technology Stack
- **Backend**: Spring Boot 3.5.7, Java 21
- **Database**: MySQL / PostgreSQL (configurable)
- **Frontend**: Thymeleaf, Bootstrap 5.3.2, Font Awesome 6.4.0
- **Build Tool**: Maven
- **ORM**: Spring Data JPA with Hibernate
- **Additional**: Lombok for code generation

## Project Structure
```
CoursePortal/
├── src/main/java/com/courseportal/CoursePortal/
│   ├── api/                    # REST API Controllers
│   │   ├── StudentRestController.java
│   │   ├── CourseRestController.java
│   │   └── EnrollmentRestController.java
│   ├── controller/             # Web Controllers
│   │   ├── HomeController.java
│   │   ├── StudentController.java
│   │   ├── CourseController.java
│   │   └── EnrollmentController.java
│   ├── model/                  # Entity Models
│   │   ├── Student.java
│   │   ├── Course.java
│   │   └── Enrollment.java
│   ├── repository/             # JPA Repositories
│   │   ├── StudentRepository.java
│   │   ├── CourseRepository.java
│   │   └── EnrollmentRepository.java
│   ├── service/                # Business Logic
│   │   ├── StudentService.java
│   │   ├── CourseService.java
│   │   └── EnrollmentService.java
│   └── CoursePortalApplication.java
├── src/main/resources/
│   ├── templates/              # Thymeleaf HTML Templates
│   │   ├── home.html
│   │   ├── students/
│   │   ├── courses/
│   │   └── enrollments/
│   ├── static/
│   │   ├── css/style.css
│   │   └── js/script.js
│   └── application.properties
└── pom.xml
```

## Database Schema

### Students Table
| Column      | Type         | Constraints        |
|-------------|--------------|-------------------|
| student_id  | BIGINT       | Primary Key, Auto |
| name        | VARCHAR(50)  | Not Null          |
| email       | VARCHAR(100) | Unique, Not Null  |
| contact_no  | VARCHAR(20)  |                   |
| age         | INTEGER      |                   |
| department  | VARCHAR(50)  |                   |
| join_date   | TIMESTAMP    | Default NOW()     |

### Courses Table
| Column       | Type         | Constraints        |
|--------------|--------------|-------------------|
| course_id    | BIGINT       | Primary Key, Auto |
| name         | VARCHAR(100) | Not Null          |
| description  | TEXT         |                   |
| max_capacity | INTEGER      | Not Null          |
| duration     | VARCHAR(50)  |                   |
| created_at   | TIMESTAMP    | Default NOW()     |

### Enrollments Table
| Column        | Type        | Constraints                   |
|---------------|-------------|------------------------------|
| enrollment_id | BIGINT      | Primary Key, Auto            |
| student_id    | BIGINT      | Foreign Key → students       |
| course_id     | BIGINT      | Foreign Key → courses        |
| enrolled_on   | TIMESTAMP   | Default NOW()                |
| status        | VARCHAR(20) | 'ENROLLED' or 'WAITLISTED'   |

## Setup Instructions

### Prerequisites
- Java 21 or higher
- MySQL 8.0+ or PostgreSQL 12+
- Maven 3.8+

### Database Configuration

1. Create a database:
```sql
CREATE DATABASE course_portal_db;
```

2. Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/course_portal_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### Running the Application

1. **Clone and Navigate**:
```bash
cd CoursePortal/CoursePortal
```

2. **Build the Project**:
```bash
./mvnw clean install
```

3. **Run the Application**:
```bash
./mvnw spring-boot:run
```

4. **Access the Application**:
- Web Interface: `http://localhost:8080`
- REST API: `http://localhost:8080/api`

## Usage Guide

### Web Interface

#### Home Dashboard
- Navigate to `http://localhost:8080`
- View available courses and quick access to all features

#### Student Management
- **View All**: `/students/view`
- **Add New**: `/students/add`
- **Edit**: Click "Edit" button on student list
- **Delete**: Click "Delete" button with confirmation

#### Course Management
- **View All**: `/courses`
- **Add New**: `/courses/add`
- **Edit**: Click "Edit" button on course list
- **Delete**: Click "Delete" button with confirmation

#### Enrollment Management
- **View All**: `/enrollments`
- **Enroll Student**: `/enrollments/add`
- **View Waitlist**: Click "View Waitlist" for any course
- **Update Status**: Edit enrollment to change status

### REST API Endpoints

#### Students API
```http
GET    /api/students           # Get all students
GET    /api/students/{id}      # Get student by ID
POST   /api/students           # Create new student
PUT    /api/students/{id}      # Update student
DELETE /api/students/{id}      # Delete student
```

#### Courses API
```http
GET    /api/courses            # Get all courses
GET    /api/courses/{id}       # Get course by ID
POST   /api/courses            # Create new course
PUT    /api/courses/{id}       # Update course
DELETE /api/courses/{id}       # Delete course
```

#### Enrollments API
```http
GET    /api/enrollments                        # Get all enrollments
GET    /api/enrollments/{id}                   # Get enrollment by ID
GET    /api/enrollments/student/{studentId}   # Get student's enrollments
GET    /api/enrollments/course/{courseId}     # Get course enrollments
GET    /api/enrollments/waitlist/course/{id}  # Get waitlist for course
POST   /api/enrollments                        # Enroll student
PUT    /api/enrollments/{id}/update-status     # Update enrollment status
DELETE /api/enrollments/{id}                   # Delete enrollment
```

### API Examples

**Enroll a Student**:
```bash
curl -X POST http://localhost:8080/api/enrollments \
  -H "Content-Type: application/json" \
  -d '{"studentId": 1, "courseId": 1}'
```

**Create a Course**:
```bash
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Introduction to Java",
    "description": "Learn Java programming fundamentals",
    "maxCapacity": 30,
    "duration": "8 weeks"
  }'
```

## Key Features Explained

### Automatic Waitlist Management
When a student enrolls in a course:
- If spots are available → Status: `ENROLLED`
- If course is full → Status: `WAITLISTED`

### Auto-Promotion from Waitlist
When an enrolled student drops a course:
- System automatically checks waitlist
- Promotes earliest waitlisted student(s) to enrolled
- Maintains fairness with FIFO (First In, First Out)

### Course Capacity Enforcement
- Real-time capacity checking
- Prevents over-enrollment
- Maintains data integrity

## Testing the Application

### Manual Testing Checklist
1. ✅ Create multiple students
2. ✅ Create courses with different capacities
3. ✅ Enroll students until course is full
4. ✅ Verify waitlist functionality
5. ✅ Delete an enrolled student
6. ✅ Verify auto-promotion from waitlist
7. ✅ Test all CRUD operations
8. ✅ Test REST API endpoints

## Future Enhancements
- Role-based authentication (Admin/Student)
- Email notifications for enrollment/waitlist status
- Course prerequisites
- Grade management
- Attendance tracking
- Report generation (PDF/Excel)
- Advanced search and filtering
- Course schedule management

## Troubleshooting

### Common Issues

**Database Connection Error**:
- Verify MySQL/PostgreSQL is running
- Check database credentials in `application.properties`
- Ensure database exists

**Port Already in Use**:
- Change port in `application.properties`:
```properties
server.port=8081
```

**Build Errors**:
- Ensure Java 21 is installed: `java -version`
- Clean and rebuild: `./mvnw clean install -U`

## License
This project is created for educational purposes.

## Author
College Project - Course Portal Team

## Contact
For questions or support, contact your project supervisor.
