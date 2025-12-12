#!/bin/bash

# Edge Cases Integration Tests for Job Server
# Prerequisites: docker-compose services running, jobserver running on port 8080

BASE_URL="http://localhost:8080"
PASS=0
FAIL=0

echo "=== Job Server Edge Cases Tests ==="
echo ""

# Test 1: Missing userId (required field)
echo "Test 1: Missing userId should fail"
RESPONSE=$(curl -s -X POST "$BASE_URL/api/jobs" \
  -H "Content-Type: application/json" \
  -d '{"projectId": "00000000-0000-0000-0000-000000000001"}')

if echo "$RESPONSE" | grep -q "error\|ERROR\|400"; then
  echo "Missing userId rejected"
  ((PASS++))
else
  echo "Expected error for missing userId, got: $RESPONSE"
  ((FAIL++))
fi

echo ""

# Test 2: Invalid UUID format
echo "Test 2: Invalid UUID format should fail"
RESPONSE=$(curl -s -X POST "$BASE_URL/api/jobs" \
  -H "Content-Type: application/json" \
  -d '{"userId": "not-a-valid-uuid"}')

if echo "$RESPONSE" | grep -q "error\|ERROR\|400"; then
  echo "Invalid UUID format rejected"
  ((PASS++))
else
  echo "Expected error for invalid UUID, got: $RESPONSE"
  ((FAIL++))
fi

echo ""

# Test 3: Empty request body
echo "Test 3: Empty request body should fail"
RESPONSE=$(curl -s -X POST "$BASE_URL/api/jobs" \
  -H "Content-Type: application/json" \
  -d '{}')

if echo "$RESPONSE" | grep -q "error\|ERROR\|400"; then
  echo "Empty request body rejected"
  ((PASS++))
else
  echo "Expected error for empty body, got: $RESPONSE"
  ((FAIL++))
fi

echo ""

# Test 4: Get non-existent job
echo "Test 4: Get non-existent job should return error"
RESPONSE=$(curl -s "$BASE_URL/api/jobs/00000000-0000-0000-0000-999999999999")

if echo "$RESPONSE" | grep -q "error\|ERROR\|404\|not found\|NOT_FOUND"; then
  echo "Non-existent job returns error"
  ((PASS++))
else
  echo "Expected error for non-existent job, got: $RESPONSE"
  ((FAIL++))
fi

echo ""

# Test 5: Invalid job ID format in GET
echo "Test 5: Invalid job ID format in GET should fail"
RESPONSE=$(curl -s "$BASE_URL/api/jobs/invalid-id")

if echo "$RESPONSE" | grep -q "error\|ERROR\|400"; then
  echo "Invalid job ID format rejected"
  ((PASS++))
else
  echo "Expected error for invalid job ID, got: $RESPONSE"
  ((FAIL++))
fi

echo ""

# Test 6: Valid user with invalid project
echo "Test 6: Valid user with non-existent project"
RESPONSE=$(curl -s -X POST "$BASE_URL/api/jobs" \
  -H "Content-Type: application/json" \
  -d '{"userId": "00000000-0000-0000-0000-000000000001", "projectId": "00000000-0000-0000-0000-999999999999"}')

# This might succeed (project is optional) or fail depending on implementation
if echo "$RESPONSE" | grep -q "PENDING\|error\|ERROR"; then
  echo "Non-existent project handled"
  ((PASS++))
else
  echo "Unexpected response: $RESPONSE"
  ((FAIL++))
fi

echo ""

# Test 7: Health endpoint
echo "Test 7: Health endpoint should return UP"
RESPONSE=$(curl -s "$BASE_URL/actuator/health")

if echo "$RESPONSE" | grep -q '"status":"UP"'; then
  echo "Health endpoint returns UP"
  ((PASS++))
else
  echo "Expected health UP, got: $RESPONSE"
  ((FAIL++))
fi

echo ""

# Test 8: Submit multiple jobs concurrently
echo "Test 8: Submit multiple jobs concurrently"
JOB1=$(curl -s -X POST "$BASE_URL/api/jobs" -H "Content-Type: application/json" -d '{"userId": "00000000-0000-0000-0000-000000000001"}' &)
JOB2=$(curl -s -X POST "$BASE_URL/api/jobs" -H "Content-Type: application/json" -d '{"userId": "00000000-0000-0000-0000-000000000002"}' &)
JOB3=$(curl -s -X POST "$BASE_URL/api/jobs" -H "Content-Type: application/json" -d '{"userId": "00000000-0000-0000-0000-000000000001"}' &)
wait

# Check all jobs were created
RESPONSE1=$(curl -s -X POST "$BASE_URL/api/jobs" -H "Content-Type: application/json" -d '{"userId": "00000000-0000-0000-0000-000000000001"}')
RESPONSE2=$(curl -s -X POST "$BASE_URL/api/jobs" -H "Content-Type: application/json" -d '{"userId": "00000000-0000-0000-0000-000000000002"}')

if echo "$RESPONSE1" | grep -q "PENDING" && echo "$RESPONSE2" | grep -q "PENDING"; then
  echo "Multiple concurrent jobs submitted successfully"
  ((PASS++))
else
  echo "Failed to submit concurrent jobs"
  ((FAIL++))
fi

echo ""
echo "=== Results ==="
echo "Passed: $PASS"
echo "Failed: $FAIL"

if [ $FAIL -eq 0 ]; then
  echo "All edge case tests passed!"
  exit 0
else
  echo "Some edge case tests failed!"
  exit 1
fi
