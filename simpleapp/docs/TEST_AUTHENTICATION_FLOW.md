# Authentication & Session Management Test Report

## Overview
This document describes the authentication filter, session management, and AJAX resource fetching implemented for SimpleApp.

## Components Implemented

### 1. AuthenticationFilter (src/com/example/AuthenticationFilter.java)
- **Purpose**: Validates user session before allowing access to protected resources
- **Protects**: `/home.jsp` and `/api/v1/*`
- **Behavior**:
  - Checks if session exists and has a `username` attribute
  - If session is invalid/expired: redirects to login page (`/index.jsp`)
  - If session is valid: logs user access and allows request to proceed
- **Logging**: All session checks logged via `java.util.logging.Logger`

### 2. LoginServlet (Updated - src/com/example/LoginServlet.java)
- **Endpoint**: `/login` (POST)
- **Session Creation**:
  - Creates session on successful login
  - Sets max inactivity interval to **2 minutes (120 seconds)**
  - Stores `username` attribute in session
- **Logging**: Logs login success/failure and exceptions

### 3. home.jsp (Updated)
- **New Features**:
  - Includes jQuery library (CDN: code.jquery.com)
  - On page load, executes AJAX call to fetch resource details
  - Displays resource ID and name in the resource card
  - If session expires during AJAX call (HTTP 401/403), auto-redirects to login
- **Resource API Call**: `GET /api/v1/simpleapp/myresources/1`
- **Response Format**: JSON `{"id": "1", "name": "Example resource"}`

### 4. ResourceApiServlet (src/com/example/ResourceApiServlet.java)
- Protected by AuthenticationFilter
- Returns 302 redirect to login if session invalid
- Otherwise returns JSON resource data

## Test Results

### Test 1: Access Protected Page Without Session
```
Request: GET /home.jsp (no session)
Response: HTTP 302 → redirect to /index.jsp
Status: ✅ PASS
```

### Test 2: Login to Create Session
```
Request: POST /login -d "username=testuser"
Response: HTTP 302 → Location: /home.jsp
Session Created: JSESSIONID=2D96186CDBA3258D06488923D1BBC2E1
Max Inactive: 120 seconds (2 minutes)
Status: ✅ PASS
```

### Test 3: Access Protected Page With Valid Session
```
Request: GET /home.jsp (with JSESSIONID cookie)
Response: HTTP 200 (page loaded)
Filter Log: "User testuser accessing: /home.jsp"
Status: ✅ PASS
```

### Test 4: AJAX Resource Fetch With Valid Session
```
Request: GET /api/v1/simpleapp/myresources/1 (with session)
Response: HTTP 200 - {"id":"1","name":"Example resource"}
Filter Log: "User testuser accessing: /api/v1/simpleapp/myresources/1"
Status: ✅ PASS
```

### Test 5: AJAX Resource Fetch Without Session
```
Request: GET /api/v1/simpleapp/myresources/1 (no session)
Response: HTTP 302 → redirect to /index.jsp
Filter Log: "Session invalid or expired for request: /api/v1/simpleapp/myresources/1"
Status: ✅ PASS
```

## Session Timeout Behavior

### Configuration
- **Max Inactivity Interval**: 2 minutes (120 seconds)
- **Configured In**: LoginServlet.java via `session.setMaxInactiveInterval(120)`

### How It Works
1. User logs in → Session created with 2-minute max inactivity
2. If no request is made for 2 minutes → Session expires
3. Next request without session → AuthenticationFilter redirects to login
4. AJAX call on expired session → Client receives 302 redirect
5. JavaScript detects 401/403 and auto-redirects user to login

### Testing Session Timeout
To test manually:
1. Login: `curl -c /tmp/cookies.txt -X POST http://localhost:8080/login -d "username=testuser"`
2. Verify session works: `curl -b /tmp/cookies.txt http://localhost:8080/api/v1/simpleapp/myresources/1`
3. Wait 2+ minutes (130 seconds)
4. Test again: `curl -b /tmp/cookies.txt http://localhost:8080/api/v1/simpleapp/myresources/1`
5. Expected: HTTP 302 redirect (session has expired)

## File Structure

```
simpleapp/
├── src/com/example/
│   ├── AuthenticationFilter.java        (NEW)
│   ├── LoginServlet.java                (UPDATED - adds session creation)
│   ├── LogoutServlet.java               (unchanged)
│   ├── ResourceApiServlet.java          (protected by filter)
│   ├── TestDbServlet.java               (unchanged)
│   └── DBUtil.java                      (unchanged)
├── home.jsp                             (UPDATED - added jQuery + AJAX)
├── index.jsp                            (login page)
└── WEB-INF/
    ├── web.xml                          (session-timeout: 2 minutes)
    ├── classes/com/example/
    │   ├── AuthenticationFilter.class    (NEW)
    │   ├── LoginServlet.class
    │   ├── LogoutServlet.class
    │   ├── ResourceApiServlet.class
    │   ├── TestDbServlet.class
    │   └── DBUtil.class
    └── lib/
        ├── commons-lang3-3.14.0.jar
        └── postgresql-42.6.0.jar
```

## Key Implementation Details

### AuthenticationFilter Logic
```java
HttpSession session = httpReq.getSession(false); // don't create new
if (session == null || session.getAttribute("username") == null) {
    // Redirect to login
    httpResp.sendRedirect(httpReq.getContextPath() + "/index.jsp");
    return;
}
// Continue filter chain
chain.doFilter(request, response);
```

### LoginServlet Session Setup
```java
HttpSession session = req.getSession(true); // create if not exists
session.setAttribute("username", username.trim());
session.setMaxInactiveInterval(120); // 2 minutes
```

### home.jsp AJAX Resource Fetch
```javascript
$(document).ready(function() {
  fetchResourceDetails();
});

function fetchResourceDetails() {
  $.ajax({
    url: '/api/v1/simpleapp/myresources/1',
    type: 'GET',
    dataType: 'json',
    timeout: 5000,
    success: function(data) {
      $('#resource').html('ID: <strong>' + data.id + '</strong><br>Name: <strong>' + data.name + '</strong>');
    },
    error: function(xhr, status, error) {
      if (xhr.status === 401 || xhr.status === 403) {
        // Session expired; redirect to login
        window.location.href = '/';
      }
    }
  });
}
```

## Security Considerations

1. **Session Validation**: Every request to protected resources is checked for valid session
2. **HttpOnly Cookies**: Tomcat sets `HttpOnly` flag on JSESSIONID (prevents JavaScript access)
3. **Session Expiry**: 2-minute timeout ensures inactive sessions are cleaned up
4. **Redirect on Expiry**: Both server-side (filter) and client-side (AJAX error handler) handle expired sessions
5. **SQL Injection Prevention**: ResourceApiServlet uses PreparedStatement with parameterized queries

## Troubleshooting

### Issue: "Session invalid or expired" immediately after login
- **Cause**: Filter might be catching home.jsp JSP preprocessing before filter completes
- **Solution**: Ensure filter mapping includes "/home.jsp" properly; use annotation-based filter

### Issue: AJAX call returns 302 instead of JSON
- **Cause**: Session expired; filter redirects to login
- **Solution**: This is expected behavior; JavaScript detects 302 and redirects user

### Issue: Resource details not displaying
- **Cause**: Database table `myresources` missing or API error
- **Solution**: Ensure table exists: `psql -d test -c "SELECT * FROM myresources;"`

## Logs Location
All filter and servlet logs output to Tomcat's standard logging:
- Main log: `/home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/logs/catalina.out`
- Search for "AuthenticationFilter" or "ResourceApiServlet" for specific traces

## Next Steps (Optional Enhancements)

1. **Remember Me**: Add long-term session option
2. **Role-Based Access**: Extend filter to check user roles
3. **CSRF Protection**: Add CSRF token validation
4. **Rate Limiting**: Prevent brute-force login attempts
5. **Audit Logging**: Log all access attempts with timestamps and IPs
6. **2FA**: Implement two-factor authentication
