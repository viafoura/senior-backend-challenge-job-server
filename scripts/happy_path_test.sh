#!/bin/bash

# Happy Path Integration Tests for Job Server
# Prerequisites: docker-compose services running, jobserver running on port 8080

BASE_URL="http://localhost:8080"
PASS=0
FAIL=0

echo "=== Job Server Happy Path Tests ==="
echo ""

# Test 1: Submit job with user and project
echo "Test 1: Submit job with user and project"
RESPONSE=$(curl -s -X POST "$BASE_URL/api/jobs" \
  -H "Content-Type: application/json" \
  -d '{"userId": "00000000-0000-0000-0000-000000000001", "projectId": "00000000-0000-0000-0000-000000000001"}')

JOB_ID=$(echo "$RESPONSE" | sed 's/.*"jobId":"\([^"]*\)".*/\1/')
STATUS=$(echo "$RESPONSE" | sed 's/.*"status":"\([^"]*\)".*/\1/')

if [ "$STATUS" = "PENDING" ]; then
  echo "Job submitted with status PENDING"
  ((PASS++))
else
  echo "Expected PENDING, got: $STATUS"
  ((FAIL++))
fi

# Wait for async processing
echo "  Waiting for async processing..."
sleep 5

# Check job completed
RESULT=$(curl -s "$BASE_URL/api/jobs/$JOB_ID")
FINAL_STATUS=$(echo "$RESULT" | sed 's/.*"status":"\([^"]*\)".*/\1/')

if [ "$FINAL_STATUS" = "COMPLETED" ]; then
  echo "Job completed successfully"
  ((PASS++))
else
  echo "Expected COMPLETED, got: $FINAL_STATUS"
  ((FAIL++))
fi

echo ""

# Test 2: Submit job without project (optional field)
echo "Test 2: Submit job without project"
RESPONSE=$(curl -s -X POST "$BASE_URL/api/jobs" \
  -H "Content-Type: application/json" \
  -d '{"userId": "00000000-0000-0000-0000-000000000002"}')

JOB_ID=$(echo "$RESPONSE" | sed 's/.*"jobId":"\([^"]*\)".*/\1/')
STATUS=$(echo "$RESPONSE" | sed 's/.*"status":"\([^"]*\)".*/\1/')

if [ "$STATUS" = "PENDING" ]; then
  echo "Job submitted without project"
  ((PASS++))
else
  echo "Expected PENDING, got: $STATUS"
  ((FAIL++))
fi

sleep 5

RESULT=$(curl -s "$BASE_URL/api/jobs/$JOB_ID")
FINAL_STATUS=$(echo "$RESULT" | sed 's/.*"status":"\([^"]*\)".*/\1/')

if [ "$FINAL_STATUS" = "COMPLETED" ]; then
  echo "Job completed successfully"
  ((PASS++))
else
  echo "Expected COMPLETED, got: $FINAL_STATUS"
  ((FAIL++))
fi

echo ""

# Test 3: Invalid user validation
echo "Test 3: Invalid user should fail"
RESPONSE=$(curl -s -X POST "$BASE_URL/api/jobs" \
  -H "Content-Type: application/json" \
  -d '{"userId": "00000000-0000-0000-0000-000000000099"}')

if echo "$RESPONSE" | grep -q "VALIDATION_ERROR"; then
  echo "Invalid user rejected correctly"
  ((PASS++))
else
  echo "Expected VALIDATION_ERROR"
  ((FAIL++))
fi

echo ""
echo "=== Results ==="
echo "Passed: $PASS"
echo "Failed: $FAIL"

if [ $FAIL -eq 0 ]; then
  echo "All tests passed!"
  exit 0
else
  echo "Some tests failed!"
  exit 1
fi
