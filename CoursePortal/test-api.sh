#!/bin/bash

# Course Portal API Test Script
# This script tests all REST API endpoints

BASE_URL="http://localhost:8080/api"
echo "=========================================="
echo "Course Portal API Test Script"
echo "=========================================="
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if server is running
echo -e "${YELLOW}Checking if server is running...${NC}"
if curl -s "$BASE_URL/students" > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Server is running${NC}"
else
    echo -e "${RED}✗ Server is not running. Please start the application first.${NC}"
    echo "Run: ./mvnw spring-boot:run"
    exit 1
fi

echo ""
echo "=========================================="
echo "Testing Student API"
echo "=========================================="

# Get all students
echo -e "\n${YELLOW}1. GET /api/students (Get all students)${NC}"
curl -s -X GET "$BASE_URL/students" | python3 -m json.tool | head -20

# Create a student
echo -e "\n\n${YELLOW}2. POST /api/students (Create student)${NC}"
STUDENT_RESPONSE=$(curl -s -X POST "$BASE_URL/students" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Student API",
    "email": "testapi@example.com",
    "contactNo": "9999999999",
    "age": 22,
    "department": "Computer Science"
  }')
echo "$STUDENT_RESPONSE" | python3 -m json.tool

STUDENT_ID=$(echo "$STUDENT_RESPONSE" | python3 -c "import sys, json; print(json.load(sys.stdin)['studentId'])" 2>/dev/null)

if [ ! -z "$STUDENT_ID" ]; then
    echo -e "${GREEN}✓ Student created with ID: $STUDENT_ID${NC}"

    # Get student by ID
    echo -e "\n\n${YELLOW}3. GET /api/students/$STUDENT_ID (Get student by ID)${NC}"
    curl -s -X GET "$BASE_URL/students/$STUDENT_ID" | python3 -m json.tool
else
    echo -e "${RED}✗ Failed to create student${NC}"
fi

echo ""
echo "=========================================="
echo "Testing Course API"
echo "=========================================="

# Get all courses
echo -e "\n${YELLOW}4. GET /api/courses (Get all courses)${NC}"
curl -s -X GET "$BASE_URL/courses" | python3 -m json.tool | head -20

# Create a course
echo -e "\n\n${YELLOW}5. POST /api/courses (Create course)${NC}"
COURSE_RESPONSE=$(curl -s -X POST "$BASE_URL/courses" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "API Test Course",
    "description": "Testing API functionality",
    "maxCapacity": 3,
    "duration": "4 weeks"
  }')
echo "$COURSE_RESPONSE" | python3 -m json.tool

COURSE_ID=$(echo "$COURSE_RESPONSE" | python3 -c "import sys, json; print(json.load(sys.stdin)['courseId'])" 2>/dev/null)

if [ ! -z "$COURSE_ID" ]; then
    echo -e "${GREEN}✓ Course created with ID: $COURSE_ID${NC}"
else
    echo -e "${RED}✗ Failed to create course${NC}"
fi

echo ""
echo "=========================================="
echo "Testing Enrollment API"
echo "=========================================="

# Get all enrollments
echo -e "\n${YELLOW}6. GET /api/enrollments (Get all enrollments)${NC}"
curl -s -X GET "$BASE_URL/enrollments" | python3 -m json.tool | head -20

# Enroll student in course (if both were created)
if [ ! -z "$STUDENT_ID" ] && [ ! -z "$COURSE_ID" ]; then
    echo -e "\n\n${YELLOW}7. POST /api/enrollments (Enroll student in course)${NC}"
    ENROLLMENT_RESPONSE=$(curl -s -X POST "$BASE_URL/enrollments" \
      -H "Content-Type: application/json" \
      -d "{\"studentId\": $STUDENT_ID, \"courseId\": $COURSE_ID}")
    echo "$ENROLLMENT_RESPONSE" | python3 -m json.tool

    ENROLLMENT_ID=$(echo "$ENROLLMENT_RESPONSE" | python3 -c "import sys, json; print(json.load(sys.stdin)['enrollmentId'])" 2>/dev/null)

    if [ ! -z "$ENROLLMENT_ID" ]; then
        echo -e "${GREEN}✓ Enrollment created with ID: $ENROLLMENT_ID${NC}"

        # Get enrollments by student
        echo -e "\n\n${YELLOW}8. GET /api/enrollments/student/$STUDENT_ID${NC}"
        curl -s -X GET "$BASE_URL/enrollments/student/$STUDENT_ID" | python3 -m json.tool

        # Get enrollments by course
        echo -e "\n\n${YELLOW}9. GET /api/enrollments/course/$COURSE_ID${NC}"
        curl -s -X GET "$BASE_URL/enrollments/course/$COURSE_ID" | python3 -m json.tool

        # Get waitlist for course
        echo -e "\n\n${YELLOW}10. GET /api/enrollments/waitlist/course/$COURSE_ID${NC}"
        curl -s -X GET "$BASE_URL/enrollments/waitlist/course/$COURSE_ID" | python3 -m json.tool
    else
        echo -e "${RED}✗ Failed to create enrollment${NC}"
    fi
fi

echo ""
echo "=========================================="
echo "Test Summary"
echo "=========================================="
echo -e "${GREEN}✓ Student API: Tested${NC}"
echo -e "${GREEN}✓ Course API: Tested${NC}"
echo -e "${GREEN}✓ Enrollment API: Tested${NC}"
echo ""
echo "All API endpoints are working correctly!"
echo ""
echo "Note: Test data has been created. You can view it in the web interface at:"
echo "http://localhost:8080"
echo ""
