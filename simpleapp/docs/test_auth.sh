#!/bin/bash
# SimpleApp Authentication - Quick Test Commands
# Run these commands to test the authentication system

echo "=========================================="
echo "SimpleApp Authentication Test Suite"
echo "=========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Configuration
BASE_URL="http://localhost:8080"
COOKIE_FILE="/tmp/simpleapp_test_cookies.txt"

echo "Base URL: $BASE_URL"
echo "Cookie File: $COOKIE_FILE"
echo ""

# Test 1: Access protected page without session
echo -e "${YELLOW}[TEST 1] Access /home.jsp without session (should redirect)${NC}"
echo "Command: curl -i -L $BASE_URL/home.jsp"
echo ""
response=$(curl -s -i -L "$BASE_URL/home.jsp" 2>&1 | head -20)
if echo "$response" | grep -q "302\|Location.*index"; then
    echo -e "${GREEN}✅ PASS${NC} - Got 302 redirect to login"
else
    echo -e "${RED}❌ FAIL${NC} - Unexpected response"
fi
echo ""

# Test 2: Access API without session
echo -e "${YELLOW}[TEST 2] Access /api/v1/... without session (should redirect)${NC}"
echo "Command: curl -i $BASE_URL/api/v1/simpleapp/myresources/1"
echo ""
response=$(curl -s -i "$BASE_URL/api/v1/simpleapp/myresources/1" 2>&1 | head -10)
if echo "$response" | grep -q "302\|Location.*index"; then
    echo -e "${GREEN}✅ PASS${NC} - Got 302 redirect to login"
else
    echo -e "${RED}❌ FAIL${NC} - Unexpected response"
fi
echo ""

# Test 3: Login
echo -e "${YELLOW}[TEST 3] Login with username (POST /login)${NC}"
echo "Command: curl -c $COOKIE_FILE -X POST -d 'username=testuser' $BASE_URL/login"
echo ""
response=$(curl -s -i -c "$COOKIE_FILE" -X POST -d "username=testuser" "$BASE_URL/login" 2>&1 | head -15)
if echo "$response" | grep -q "302\|Location.*home"; then
    echo -e "${GREEN}✅ PASS${NC} - Login successful, got redirect to /home.jsp"
    echo "Session cookies saved to: $COOKIE_FILE"
else
    echo -e "${RED}❌ FAIL${NC} - Login failed"
fi
echo ""

# Test 4: Access protected page with session
echo -e "${YELLOW}[TEST 4] Access /home.jsp with valid session${NC}"
echo "Command: curl -b $COOKIE_FILE $BASE_URL/home.jsp"
echo ""
response=$(curl -s -b "$COOKIE_FILE" "$BASE_URL/home.jsp" 2>&1)
if echo "$response" | grep -q "id=\"resource\"\|testuser\|Welcome"; then
    echo -e "${GREEN}✅ PASS${NC} - Got home page successfully"
    echo "Response snippet:"
    echo "$response" | grep -A 2 "id=\"resource\"" | head -3
else
    echo -e "${RED}❌ FAIL${NC} - Failed to access protected page"
fi
echo ""

# Test 5: Access API with session
echo -e "${YELLOW}[TEST 5] Access /api/v1/... with valid session${NC}"
echo "Command: curl -b $COOKIE_FILE $BASE_URL/api/v1/simpleapp/myresources/1"
echo ""
response=$(curl -s -b "$COOKIE_FILE" "$BASE_URL/api/v1/simpleapp/myresources/1" 2>&1)
if echo "$response" | grep -q "\"id\"\|\"name\""; then
    echo -e "${GREEN}✅ PASS${NC} - Got JSON response from API"
    echo "Response:"
    echo "$response"
else
    echo -e "${RED}❌ FAIL${NC} - Failed to get API response"
fi
echo ""

# Test 6: Check logs
echo -e "${YELLOW}[TEST 6] Check Tomcat logs for filter activity${NC}"
echo "Checking: /home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/logs/catalina.out"
echo ""
log_file="/home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/logs/catalina.out"
if [ -f "$log_file" ]; then
    echo "Recent AuthenticationFilter logs:"
    tail -n 20 "$log_file" | grep -i "authentication\|User.*accessing" || echo "No filter logs found"
    echo ""
    echo "Recent LoginServlet logs:"
    tail -n 20 "$log_file" | grep -i "Login success" || echo "No login logs found"
    echo -e "${GREEN}✅ PASS${NC} - Log file exists"
else
    echo -e "${RED}❌ FAIL${NC} - Log file not found"
fi
echo ""

# Test 7: Test with curl JSON pretty-print
echo -e "${YELLOW}[TEST 7] Pretty-print API response (requires jq)${NC}"
echo "Command: curl -s -b $COOKIE_FILE $BASE_URL/api/v1/simpleapp/myresources/1 | jq ."
echo ""
if command -v jq &> /dev/null; then
    curl -s -b "$COOKIE_FILE" "$BASE_URL/api/v1/simpleapp/myresources/1" | jq . 2>/dev/null && \
        echo -e "${GREEN}✅ PASS${NC} - JSON pretty-printed" || \
        echo -e "${RED}❌ FAIL${NC} - jq failed to parse"
else
    echo "⚠️  jq not installed. Install with: apt-get install jq"
fi
echo ""

# Test 8: Session timeout test (informational)
echo -e "${YELLOW}[TEST 8] Session timeout information${NC}"
echo "Session max inactivity: 120 seconds (2 minutes)"
echo ""
echo "To test session timeout:"
echo "1. Verify session works: curl -b $COOKIE_FILE $BASE_URL/api/v1/simpleapp/myresources/1"
echo "2. Wait 120+ seconds"
echo "3. Test again - should get 302 redirect"
echo ""
echo "Current time: $(date)"
echo "Expected expiry: $(date -d '+120 seconds')"
echo ""

# Test 9: Logout test
echo -e "${YELLOW}[TEST 9] Logout (GET /logout)${NC}"
echo "Command: curl -b $COOKIE_FILE -i $BASE_URL/logout"
echo ""
response=$(curl -s -i -b "$COOKIE_FILE" "$BASE_URL/logout" 2>&1 | head -10)
if echo "$response" | grep -q "302\|302 Found"; then
    echo -e "${GREEN}✅ PASS${NC} - Logout successful (got 302 redirect)"
else
    echo -e "${RED}⚠️  WARNING${NC} - Unexpected response from logout"
fi
echo ""

# Summary
echo "=========================================="
echo "Test Summary"
echo "=========================================="
echo ""
echo "Test files generated:"
echo "- Cookie file: $COOKIE_FILE"
echo ""
echo "Next steps:"
echo "1. Verify all tests passed (green checkmarks)"
echo "2. Check Tomcat logs: tail -f /home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/logs/catalina.out"
echo "3. Browser test: Visit http://localhost:8080 and login manually"
echo ""
echo "=========================================="
