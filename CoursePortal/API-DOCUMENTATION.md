# Course Portal REST API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication
Currently, the API does not require authentication (can be added as an enhancement).

---

## Students API

### Get All Students
**Endpoint**: `GET /api/students`

**Response**: `200 OK`
```json
[
  {
    "studentId": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "contactNo": "1234567890",
    "age": 20,
    "department": "Computer Science",
    "joinDate": "2025-01-15T10:30:00"
  }
]
```

### Get Student by ID
**Endpoint**: `GET /api/students/{id}`

**Response**: `200 OK` or `404 Not Found`

### Create Student
**Endpoint**: `POST /api/students`

**Request Body**:
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "contactNo": "1234567890",
  "age": 20,
  "department": "Computer Science"
}
```

**Response**: `201 Created`

### Update Student
**Endpoint**: `PUT /api/students/{id}`

**Request Body**:
```json
{
  "name": "John Updated",
  "email": "john.updated@example.com",
  "contactNo": "9876543210",
  "age": 21,
  "department": "Engineering"
}
```

**Response**: `200 OK` or `404 Not Found`

### Delete Student
**Endpoint**: `DELETE /api/students/{id}`

**Response**: `204 No Content` or `404 Not Found`

---

## Courses API

### Get All Courses
**Endpoint**: `GET /api/courses`

**Response**: `200 OK`
```json
[
  {
    "courseId": 1,
    "name": "Introduction to Java",
    "description": "Learn Java programming fundamentals",
    "maxCapacity": 30,
    "duration": "8 weeks",
    "createdAt": "2025-01-10T09:00:00"
  }
]
```

### Get Course by ID
**Endpoint**: `GET /api/courses/{id}`

**Response**: `200 OK` or `404 Not Found`

### Create Course
**Endpoint**: `POST /api/courses`

**Request Body**:
```json
{
  "name": "Introduction to Java",
  "description": "Learn Java programming fundamentals",
  "maxCapacity": 30,
  "duration": "8 weeks"
}
```

**Response**: `201 Created`

### Update Course
**Endpoint**: `PUT /api/courses/{id}`

**Request Body**:
```json
{
  "name": "Advanced Java Programming",
  "description": "Deep dive into Java advanced topics",
  "maxCapacity": 25,
  "duration": "10 weeks"
}
```

**Response**: `200 OK` or `404 Not Found`

### Delete Course
**Endpoint**: `DELETE /api/courses/{id}`

**Response**: `204 No Content` or `404 Not Found`

---

## Enrollments API

### Get All Enrollments
**Endpoint**: `GET /api/enrollments`

**Response**: `200 OK`
```json
[
  {
    "enrollmentId": 1,
    "student": {
      "studentId": 1,
      "name": "John Doe",
      "email": "john.doe@example.com"
    },
    "course": {
      "courseId": 1,
      "name": "Introduction to Java",
      "maxCapacity": 30
    },
    "enrolledOn": "2025-01-20T14:30:00",
    "status": "ENROLLED"
  }
]
```

### Get Enrollment by ID
**Endpoint**: `GET /api/enrollments/{id}`

**Response**: `200 OK` or `404 Not Found`

### Enroll Student in Course
**Endpoint**: `POST /api/enrollments`

**Request Body**:
```json
{
  "studentId": 1,
  "courseId": 1
}
```

**Response**: `201 Created`
```json
{
  "enrollmentId": 1,
  "student": {...},
  "course": {...},
  "enrolledOn": "2025-01-20T14:30:00",
  "status": "ENROLLED"
}
```

**Error Response**: `400 Bad Request`
```json
{
  "error": "Student already enrolled in this course"
}
```

**Note**: If course is at capacity, status will be "WAITLISTED"

### Get Enrollments by Student
**Endpoint**: `GET /api/enrollments/student/{studentId}`

**Response**: `200 OK` - List of all enrollments for the student

### Get Enrollments by Course
**Endpoint**: `GET /api/enrollments/course/{courseId}`

**Response**: `200 OK` - List of all enrollments for the course

### Update Enrollment Status
**Endpoint**: `PUT /api/enrollments/{id}/update-status`

**Request Body**:
```json
{
  "status": "ENROLLED"
}
```

**Valid Status Values**: `ENROLLED`, `WAITLISTED`

**Response**: `200 OK` or `400 Bad Request`

### Delete Enrollment
**Endpoint**: `DELETE /api/enrollments/{id}`

**Response**: `204 No Content` or `404 Not Found`

**Note**: When an enrolled student is removed, the system automatically promotes the first waitlisted student.

### Get Waitlist for Course
**Endpoint**: `GET /api/enrollments/waitlist/course/{courseId}`

**Response**: `200 OK`
```json
[
  {
    "enrollmentId": 5,
    "student": {
      "studentId": 5,
      "name": "Carol Davis"
    },
    "course": {
      "courseId": 1,
      "name": "Introduction to Java"
    },
    "enrolledOn": "2025-01-21T10:00:00",
    "status": "WAITLISTED"
  }
]
```

---

## Complete Usage Examples

### Example 1: Enroll a Student in a Course

**Step 1**: Create a student
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Johnson",
    "email": "alice@example.com",
    "contactNo": "5551234567",
    "age": 20,
    "department": "Computer Science"
  }'
```

**Step 2**: Create a course
```bash
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Web Development",
    "description": "Learn HTML, CSS, JavaScript",
    "maxCapacity": 5,
    "duration": "6 weeks"
  }'
```

**Step 3**: Enroll student in course
```bash
curl -X POST http://localhost:8080/api/enrollments \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 1,
    "courseId": 1
  }'
```

### Example 2: Check Course Waitlist

```bash
curl http://localhost:8080/api/enrollments/waitlist/course/1
```

### Example 3: View Student's Enrollments

```bash
curl http://localhost:8080/api/enrollments/student/1
```

### Example 4: Update Student Information

```bash
curl -X PUT http://localhost:8080/api/students/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Johnson Updated",
    "email": "alice.updated@example.com",
    "contactNo": "5559876543",
    "age": 21,
    "department": "Software Engineering"
  }'
```

### Example 5: Delete an Enrollment (Auto-promotes from waitlist)

```bash
curl -X DELETE http://localhost:8080/api/enrollments/1
```

---

## Error Responses

### 400 Bad Request
```json
{
  "error": "Error message describing what went wrong"
}
```

### 404 Not Found
Returned when the requested resource doesn't exist

### 500 Internal Server Error
```json
{
  "timestamp": "2025-01-20T10:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Error description"
}
```

---

## Business Logic

### Enrollment Status Logic
- When enrolling a student:
  - Check current enrollments with status "ENROLLED"
  - If count < maxCapacity → status = "ENROLLED"
  - If count >= maxCapacity → status = "WAITLISTED"

### Auto-Promotion Logic
- When an enrollment is deleted:
  - If deleted enrollment had status "ENROLLED"
  - System checks waitlist (sorted by enrolledOn)
  - Automatically promotes first waitlisted student to "ENROLLED"

### Duplicate Prevention
- System prevents duplicate enrollments (same student + same course)
- Returns error: "Student already enrolled in this course"

---

## Testing with Postman

Import this collection to test all endpoints:

1. Create a new Postman collection
2. Add requests for each endpoint
3. Set base URL as variable: `{{baseUrl}} = http://localhost:8080/api`
4. Test the complete workflow

---

## Notes
- All timestamps are in ISO 8601 format
- All IDs are auto-generated (Long type)
- Email addresses must be unique for students
- Course capacity must be > 0
- Status values are case-sensitive: "ENROLLED", "WAITLISTED"
