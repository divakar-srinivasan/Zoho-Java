# SimpleApp - Authentication & Session Flow Guide

## Quick Start Manual Testing

### Prerequisites
- Tomcat running: http://localhost:8080
- Database with `myresources` table and sample data

### Scenario 1: Normal Login & Resource Fetch

**Step 1: Open login page**
```
Browser: http://localhost:8080
```
Expected: Login form displays

**Step 2: Enter credentials and login**
```
Username: testuser
Click: Sign In
```
Expected: Redirected to home.jsp, page loads, AJAX fetches resource details

**Step 3: Verify resource displayed**
Expected: In "Resources" card, you see:
```
ID: 1
Name: Example resource
```

**Step 4: Open browser developer console (F12)**
Expected: No AJAX errors; you'll see successful fetch in console logs

---

### Scenario 2: Session Expiry on Reload

**Step 1: Login successfully (from Scenario 1)**

**Step 2: Wait 2 minutes 10 seconds** (session max inactivity = 120 seconds)

**Step 3: Reload page (F5)**
Expected: 
- Page redirects to login
- Session has expired
- Must login again

---

### Scenario 3: Direct API Access Without Session

**Step 1: Open new browser tab**

**Step 2: Try to access API directly**
```
Browser: http://localhost:8080/api/v1/simpleapp/myresources/1
```
Expected: Redirect to login page (HTTP 302)

**Step 3: Login first, then try again**
```
1. Go to http://localhost:8080
2. Login with username
3. Then access: http://localhost:8080/api/v1/simpleapp/myresources/1
```
Expected: JSON response:
```json
{"id":"1","name":"Example resource"}
```

---

### Scenario 4: AJAX Timeout on Expired Session

**Step 1: Login successfully**

**Step 2: Wait 2 minutes 10 seconds**

**Step 3: Open browser console (F12)**

**Step 4: Manually trigger AJAX call**
```javascript
// Type in console:
fetchResourceDetails();
```
Expected:
- AJAX call gets 302 redirect
- Console error logged
- Page auto-redirects to login after 1 second

---

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────┐
│                    User Browser                         │
│                                                         │
│  1. Requests /home.jsp (no session)                    │
│           ↓                                             │
└─────────────┼───────────────────────────────────────────┘
              │
              ├─→ AuthenticationFilter
              │   (check session)
              │   └─→ Session null/invalid
              │       └─→ Redirect /index.jsp
              │
              └─→ User sees login page
                  ↓
              Enter username & click Sign In
                  ↓
                  └─→ POST /login
                      ├─→ LoginServlet.doPost()
                      │   ├─→ Create session
                      │   ├─→ Set max inactivity = 120 sec
                      │   ├─→ Store username in session
                      │   └─→ Redirect /home.jsp
                      └─→ Browser stores JSESSIONID cookie
                          ↓
                      GET /home.jsp (with JSESSIONID)
                      ├─→ AuthenticationFilter
                      │   ├─→ Check session (valid)
                      │   └─→ Allow request
                      ├─→ home.jsp loads
                      ├─→ jQuery AJAX on page load
                      │   └─→ GET /api/v1/simpleapp/myresources/1
                      │       ├─→ AuthenticationFilter (check session)
                      │       ├─→ ResourceApiServlet
                      │       └─→ Return JSON
                      └─→ JavaScript displays resource data

              ↓ After 2 minutes 10 seconds (session expires)
                  
              GET /home.jsp (session expired)
              ├─→ AuthenticationFilter
              │   ├─→ Check session (expired/invalid)
              │   └─→ Redirect /index.jsp
              └─→ User sees login page again
```

---

## Key Configuration

### Session Timeout
```xml
<!-- WEB-INF/web.xml -->
<session-config>
    <session-timeout>2</session-timeout>  <!-- 2 minutes -->
</session-config>
```

Also set per-session in LoginServlet:
```java
session.setMaxInactiveInterval(120); // 120 seconds = 2 minutes
```

### Protected Paths (AuthenticationFilter)
```java
@WebFilter(filterName = "AuthenticationFilter", 
           urlPatterns = {"/home.jsp", "/api/v1/*"})
```

---

## Log Files & Debugging

### View Tomcat Logs
```bash
# Main log file
tail -f /home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/logs/catalina.out

# Search for authentication events
grep -i "AuthenticationFilter\|session" catalina.out

# Search for login events
grep -i "LoginServlet" catalina.out
```

### Sample Log Output
```
17-Nov-2025 16:17:37.968 INFO AuthenticationFilter initialized
17-Nov-2025 16:17:50.635 INFO Session invalid or expired for request: /home.jsp
17-Nov-2025 16:18:09.583 INFO User testuser accessing: /home.jsp
17-Nov-2025 16:18:09.584 INFO Login success for user: testuser
17-Nov-2025 16:18:19.579 INFO User testuser accessing: /api/v1/simpleapp/myresources/1
17-Nov-2025 16:18:29.697 INFO Session invalid or expired for request: /api/v1/simpleapp/myresources/1
```

---

## Code Changes Summary

### Files Added
- `src/com/example/AuthenticationFilter.java` - New servlet filter

### Files Modified
- `home.jsp` - Added jQuery and AJAX resource fetch
- `LoginServlet.java` - Already had session creation (no changes needed)

### Files Unchanged
- `index.jsp` (login form already present)
- `LogoutServlet.java` (already working)
- `web.xml` (session timeout already configured)

---

## Troubleshooting Checklist

- [ ] Tomcat is running: `ps aux | grep tomcat`
- [ ] WAR is deployed: `ls -la /home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/webapps/ROOT.war`
- [ ] Database is accessible: `PGPASSWORD='...' psql -h localhost -U divakar-pt8008 -d test -c "SELECT * FROM myresources;"`
- [ ] jQuery loads: Open browser DevTools → Network → check for jquery-3.6.0.min.js (status 200)
- [ ] AJAX call completes: DevTools → Console → check for log messages from fetchResourceDetails()
- [ ] Session created: Check browser cookies (F12 → Application → Cookies) for JSESSIONID

---

## Performance Notes

- **Session Timeout**: 2 minutes (120 seconds) - suitable for testing. Adjust in production as needed:
  - `session.setMaxInactiveInterval(600)` for 10 minutes
  - `session.setMaxInactiveInterval(3600)` for 1 hour

- **AJAX Timeout**: 5 seconds - adjust if network is slow:
  - Line in home.jsp: `timeout: 5000` (milliseconds)

- **Filter Performance**: Minimal overhead - only checks session attribute existence

---

## Next Steps

1. **Test in browser**: Visit http://localhost:8080 and complete all scenarios
2. **Check logs**: Monitor Tomcat logs for filter activity
3. **Verify AJAX**: Use browser DevTools console to see AJAX results
4. **Adjust timeout**: Change session timeout as needed for your use case
5. **Deploy to production**: Update credentials and increase session timeout
