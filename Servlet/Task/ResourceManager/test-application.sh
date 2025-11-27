#!/bin/bash

# Comprehensive Testing Script
# Tests login, logout, resource API, and session management

echo "========================================"
echo "ResourceManager Application Testing"
echo "========================================"
echo ""

BASE_URL="https://resourcemanager.local:8443"
CURL_OPTS="-k -s -c /tmp/cookies.txt -b /tmp/cookies.txt"

# Color codes
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

test_count=0
pass_count=0
fail_count=0

# Function to run a test
run_test() {
    local test_name="$1"
    local test_cmd="$2"
    local expected="$3"
    
    test_count=$((test_count + 1))
    
    echo -ne "Test $test_count: $test_name... "
    
    result=$(eval "$test_cmd")
    
    if [[ "$result" == *"$expected"* ]]; then
        echo -e "${GREEN}PASS${NC}"
        pass_count=$((pass_count + 1))
    else
        echo -e "${RED}FAIL${NC}"
        echo "  Expected substring: $expected"
        echo "  Got: $result"
        fail_count=$((fail_count + 1))
    fi
}

# Clear cookies
rm -f /tmp/cookies.txt

echo "Testing HTTP requests..."
echo ""

# Test 1: Verify login page is accessible
echo -e "${YELLOW}Authentication Tests${NC}"
run_test "Access login page" \
    "curl $CURL_OPTS $BASE_URL/login.jsp 2>/dev/null | grep -o 'Resource Manager'" \
    "Resource Manager"

# Test 2: Test failed login
run_test "Failed login attempt" \
    "curl $CURL_OPTS -X POST $BASE_URL/login -d 'username=wrong&password=wrong' 2>/dev/null | grep -o 'login'" \
    "login"

# Test 3: Test successful login
run_test "Successful login" \
    "curl $CURL_OPTS -X POST $BASE_URL/login -d 'username=admin&password=admin' -L 2>/dev/null | grep -o 'Welcome' | head -1" \
    "Welcome"

echo ""
echo -e "${YELLOW}API Tests${NC}"

# Test 4: Access protected resource (home page)
run_test "Access protected home page" \
    "curl $CURL_OPTS $BASE_URL/home.jsp 2>/dev/null | grep -o 'Welcome'" \
    "Welcome"

# Test 5: Resource API - Get resource details
run_test "Get resource details from API" \
    "curl $CURL_OPTS $BASE_URL/api/v1/resourcemanager/resource/1 2>/dev/null | grep -o 'id'" \
    "id"

# Test 6: Resource API - Verify JSON response
run_test "API returns valid JSON" \
    "curl $CURL_OPTS $BASE_URL/api/v1/resourcemanager/resource/1 2>/dev/null | grep -o '\"name\"'" \
    "\"name\""

echo ""
echo -e "${YELLOW}Session Tests${NC}"

# Test 7: Invalid session redirect
run_test "Invalid session redirects to login" \
    "curl $CURL_OPTS -b 'JSESSIONID=invalid' $BASE_URL/home.jsp 2>/dev/null | grep -o 'login'" \
    "login"

echo ""
echo -e "${YELLOW}Logout Tests${NC}"

# Test 8: Logout functionality
run_test "Logout clears session" \
    "curl $CURL_OPTS $BASE_URL/logout 2>/dev/null ; curl $CURL_OPTS $BASE_URL/home.jsp 2>/dev/null | grep -o 'login'" \
    "login"

echo ""
echo "========================================"
echo "Test Results"
echo "========================================"
echo -e "Total Tests: $test_count"
echo -e "${GREEN}Passed: $pass_count${NC}"
echo -e "${RED}Failed: $fail_count${NC}"

if [ $fail_count -eq 0 ]; then
    echo -e "\n${GREEN}All tests passed!${NC}"
    exit 0
else
    echo -e "\n${RED}Some tests failed. Please check the application.${NC}"
    exit 1
fi
