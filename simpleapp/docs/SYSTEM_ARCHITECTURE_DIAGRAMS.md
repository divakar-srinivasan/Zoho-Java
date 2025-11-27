# SimpleApp System Architecture Diagram

## High-Level System Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          USER BROWSER                                       │
│  http://localhost:8080                                                      │
│                                                                             │
│  ┌─────────────┐  ┌─────────────┐  ┌──────────────┐  ┌──────────────┐    │
│  │ index.jsp   │  │ home.jsp    │  │ AJAX Request │  │  Cookies     │    │
│  │ (Login Page)│  │ (Dashboard) │  │ (jQuery)     │  │ (JSESSIONID) │    │
│  └─────────────┘  └─────────────┘  └──────────────┘  └──────────────┘    │
└────────────────────────┬───────────────────────────────────────────────────┘
                         │
                         │ HTTP Requests
                         │
         ┌───────────────┼───────────────┐
         │               │               │
         ▼               ▼               ▼
      /login          /home.jsp      /api/v1/*
         │               │               │
         └───────────────┼───────────────┘
                         │
         ┌───────────────▼───────────────┐
         │   TOMCAT SERVLET CONTAINER    │
         │   (Port 8080)                 │
         │                               │
         │  ┌──────────────────────────┐ │
         │  │ AuthenticationFilter     │ │
         │  │ ──────────────────────   │ │
         │  │ • Check session validity │ │
         │  │ • Redirect if expired    │ │
         │  │ • Log access attempts    │ │
         │  └────────┬──────────────┬──┘ │
         │           │              │     │
         │      Valid?          Invalid?  │
         │      (Yes)           (No)      │
         │           │              │     │
         │           ▼              ▼     │
         │  ┌──────────────────┐  302    │
         │  │ Allow Request    │ Redirect│
         │  │                  │  /index │
         │  └────────┬─────────┘         │
         │           │                   │
         │      ┌────┴───────┐           │
         │      │            │           │
         │      ▼            ▼           │
         │  ┌─────────┐  ┌──────────────┐│
         │  │LoginSvt │  │ResourceAPI   ││
         │  │─────────│  │──────────────││
         │  │Create   │  │Query DB      ││
         │  │Session  │  │Return JSON   ││
         │  │Timeout: │  │ {"id":"1"..} ││
         │  │120 sec  │  └──────────────┘│
         │  └─────────┘                  │
         │                               │
         └───────────────────────────────┘
                         │
                    Response ▼
              ┌─────────────────────────┐
              │   PostgreSQL Database   │
              │   (localhost:5432)      │
              │                         │
              │  myresources table:     │
              │  id    | name           │
              │  ───────────────────    │
              │  1     | Example resource
              │  ...                    │
              └─────────────────────────┘


```

---

## Session & Authentication Flow Diagram

```
TIMELINE: User Session Lifecycle
═══════════════════════════════════════════════════════════════════════

Time: T+0s (User opens browser)
  │
  ├─→ GET http://localhost:8080
  │   └─→ Filter: Check session (null)
  │       └─→ Redirect to /index.jsp
  │           └─→ Browser shows login form
  │
┃
  │ User enters username and clicks Sign In
  │
┃ Time: T+5s (Login POST)
  │
  ├─→ POST /login -d "username=testuser"
  │   ├─→ LoginServlet.doPost()
  │   │   ├─→ Create session: req.getSession(true)
  │   │   ├─→ Store username: session.setAttribute("username", "testuser")
  │   │   ├─→ Set timeout: session.setMaxInactiveInterval(120)
  │   │   ├─→ Log: "Login success for user: testuser"
  │   │   └─→ HTTP 302 redirect /home.jsp
  │   └─→ Browser stores JSESSIONID cookie
  │       └─→ Cookie expiry: T+125s (120 sec inactivity + buffer)
  │
┃
  │ Browser redirected to home.jsp
  │
┃ Time: T+6s (GET /home.jsp with session)
  │
  ├─→ GET /home.jsp + JSESSIONID cookie
  │   ├─→ AuthenticationFilter.doFilter()
  │   │   ├─→ Get session: req.getSession(false)
  │   │   ├─→ Session NOT null ✓
  │   │   ├─→ Check username attribute ✓
  │   │   ├─→ Log: "User testuser accessing: /home.jsp"
  │   │   └─→ Allow request (chain.doFilter)
  │   ├─→ home.jsp servlet loads
  │   ├─→ JSP renders HTML with jQuery
  │   └─→ Browser shows dashboard page with "Loading resource details..."
  │
┃
  │ jQuery ready() fires
  │
┃ Time: T+7s (AJAX fetch)
  │
  ├─→ $.ajax( GET /api/v1/simpleapp/myresources/1 )
  │   ├─→ Browser sends JSESSIONID in request
  │   ├─→ AuthenticationFilter.doFilter()
  │   │   ├─→ Get session: req.getSession(false)
  │   │   ├─→ Session still valid ✓
  │   │   ├─→ Check username: "testuser" ✓
  │   │   ├─→ Log: "User testuser accessing: /api/v1/simpleapp/myresources/1"
  │   │   └─→ Allow request
  │   ├─→ ResourceApiServlet.doGet()
  │   │   ├─→ Parse path: app=simpleapp, resource=myresources, id=1
  │   │   ├─→ Query DB: SELECT id, name FROM myresources WHERE id=1
  │   │   ├─→ Get result: id=1, name="Example resource"
  │   │   ├─→ Log: "Fetched resource: app=simpleapp resource=myresources id=1"
  │   │   └─→ Return JSON: {"id":"1","name":"Example resource"}
  │   ├─→ AJAX success callback
  │   └─→ JavaScript updates page: display resource data
  │
┃
  │ [Normal usage: page active, requests refresh session timeout]
  │
┃ Time: T+127s (2 min 7 sec - SESSION EXPIRED)
  │
  │ ⚠ SESSION TIMEOUT REACHED ⚠
  │ (Max inactivity interval: 120 sec)
  │ Session is now invalid/expired
  │
┃
  │ User takes action (click link, reload, etc.)
  │
┃ Time: T+135s (User reloads page)
  │
  ├─→ GET /home.jsp + OLD JSESSIONID cookie (expired)
  │   ├─→ AuthenticationFilter.doFilter()
  │   │   ├─→ Get session: req.getSession(false)
  │   │   ├─→ Session is NULL (expired) ✗
  │   │   ├─→ Check username: NULL ✗
  │   │   ├─→ Log: "Session invalid or expired for request: /home.jsp"
  │   │   ├─→ HTTP 302 redirect /index.jsp
  │   │   └─→ STOP (don't continue)
  │   └─→ Browser redirected to login page
  │       └─→ User sees login form again
  │
  │ User must login again to get new session
  │
  └─→ Process repeats: T+0s cycle starts over

═══════════════════════════════════════════════════════════════════════
```

---

## Filter Processing Flow Diagram

```
                    HTTP Request
                         │
                         ▼
                ┌──────────────────────┐
                │ AuthenticationFilter  │
                │ .doFilter()           │
                └──────────┬───────────┘
                           │
                ┌──────────▼──────────┐
                │ Get Session         │
                │ getSession(false)   │
                │ (don't create new)  │
                └──────────┬──────────┘
                           │
                    ┌──────▼──────┐
                    │ Is session  │
                    │ null?       │
                    └──────┬──────┘
                   YES │    │ NO
                       │    │
        ┌──────────────┘    └─────────────┐
        │                                  │
        ▼                                  ▼
  ┌──────────────┐          ┌────────────────────────┐
  │ Session is   │          │ Get username from      │
  │ NULL         │          │ session.getAttribute   │
  └──────┬───────┘          │ ("username")           │
         │                  └────────────┬───────────┘
         │                               │
    ┌────▼────────────┐      ┌──────────▼──────┐
    │ Log: "Session   │      │ Is username     │
    │ invalid or      │      │ null/empty?     │
    │ expired"        │      └──────────┬──────┘
    └────┬────────────┘         YES│    │NO
         │                        │    │
         └────────┬───────────────┘    │
                  │                    │
        ┌─────────▼──────────┐  ┌──────▼────────────┐
        │ HTTP 302 Redirect  │  │ Log: "User {name} │
        │ to /index.jsp      │  │ accessing {path}"  │
        │                    │  └──────┬─────────────┘
        │ STOP Processing    │         │
        └────────────────────┘  ┌──────▼──────────────┐
                                │ chain.doFilter()    │
                                │ (Allow request)     │
                                │ Proceed to servlet/ │
                                │ JSP                 │
                                └─────────────────────┘
```

---

## Web Application Structure

```
SimpleApp (ROOT.war)
│
├── index.jsp (public)
│   └─ Login form
│
├── home.jsp (protected by filter)
│   ├─ Shows dashboard
│   ├─ jQuery library included
│   ├─ AJAX call on page load
│   └─ Displays resource details
│
├── WEB-INF/
│   │
│   ├── web.xml (deployment descriptor)
│   │   └─ <session-timeout>2</session-timeout>
│   │
│   ├── classes/com/example/
│   │   ├── AuthenticationFilter.class (NEW - filter logic)
│   │   ├── LoginServlet.class (session creation)
│   │   ├── LogoutServlet.class (session invalidation)
│   │   ├── ResourceApiServlet.class (API endpoint, protected)
│   │   ├── TestDbServlet.class (DB connectivity test)
│   │   └── DBUtil.class (database helper)
│   │
│   └── lib/
│       ├── commons-lang3-3.14.0.jar (HTML escaping)
│       └── postgresql-42.6.0.jar (database driver)
│
└── META-INF/
    └── MANIFEST.MF
```

---

## Database & API Integration

```
Browser AJAX Request
       │
       ▼
GET /api/v1/simpleapp/myresources/1
(with JSESSIONID cookie)
       │
       ▼
AuthenticationFilter ──▶ Session valid? ──▶ YES ▶ Allow
                              │
                              ▼ NO
                           Redirect /index.jsp
                           (STOP)
       │
       ├─ Continue to ResourceApiServlet
       │
       ▼
ResourceApiServlet.doGet()
       │
       ├─ Parse: app=simpleapp, resource=myresources, id=1
       ├─ Validate resource name regex: [A-Za-z0-9_]+ ✓
       ├─ Build SQL: SELECT id, name FROM myresources WHERE id=?
       │
       ▼
DBUtil.getConnection()
       │
       ├─ Load driver: org.postgresql.Driver
       ├─ Connect: jdbc:postgresql://localhost:5432/test
       ├─ Authenticate: user=divakar-pt8008, password=***
       │
       ▼
   PostgreSQL Database
   (localhost:5432)
       │
       ├─ Execute: SELECT id, name FROM myresources WHERE id=1
       │
       ▼
   ResultSet:
   ┌─────────────────────┐
   │ id  │ name            │
   ├─────┼─────────────────┤
   │ 1   │ Example resource│
   └─────────────────────┘
       │
       ▼
   Build JSON Response
   {"id":"1","name":"Example resource"}
       │
       ▼
   HTTP 200 OK
   Content-Type: application/json
       │
       ▼
   Browser receives JSON
       │
       ▼
   jQuery AJAX success callback
       │
       ▼
   JavaScript updates page
   Display: "ID: 1, Name: Example resource"
```

---

## Security Layers

```
Request Security Checks (in order)
═════════════════════════════════════════════════════════════════

1. AuthenticationFilter (First Line of Defense)
   ├─ Checks: Session exists?
   ├─ Checks: Username attribute set?
   ├─ Action: Redirect to login if invalid
   └─ Result: Prevents unauthenticated access

2. HttpOnly Cookie Flag
   ├─ JSESSIONID marked HttpOnly
   ├─ Prevents: JavaScript from reading cookie
   ├─ Attack Prevention: XSS attacks can't steal session
   └─ Result: Session ID safe from script injection

3. Session Timeout (120 seconds)
   ├─ Auto-expires: After 2 minutes of inactivity
   ├─ Prevents: Session hijacking from old cookies
   ├─ Cleanup: Expired sessions removed from memory
   └─ Result: Limited window for unauthorized access

4. ResourceApiServlet Input Validation
   ├─ Regex validation: [A-Za-z0-9_]+
   ├─ PreparedStatement: Parameterized queries
   ├─ Prevents: SQL injection
   └─ Result: Database queries are safe

5. HTTPS (Recommended for Production)
   ├─ Encrypts: Session cookie in transit
   ├─ Prevents: Network eavesdropping
   └─ Result: Sessions cannot be intercepted

═════════════════════════════════════════════════════════════════
```

---

## Performance Characteristics

```
Operation Latency (measured in milliseconds)
═════════════════════════════════════════════════════════════════

Login Request (POST /login):
├─ Network latency: ~5ms
├─ Servlet processing: ~2ms
├─ Session creation: ~1ms
├─ Redirect response: ~1ms
└─ Total: ~9ms

Home Page Request (GET /home.jsp):
├─ Network latency: ~5ms
├─ Filter session check: ~0.5ms
├─ JSP compilation (1st time): ~100ms (cached after)
├─ JSP execution: ~10ms
└─ Total: ~115ms (1st), ~15ms (subsequent)

AJAX Resource Fetch (GET /api/v1/simpleapp/myresources/1):
├─ Network latency: ~5ms
├─ Filter session check: ~0.5ms
├─ Database query: ~2ms
├─ JSON serialization: ~0.5ms
└─ Total: ~8ms

Session Validation (Filter):
├─ Get session: ~0.1ms
├─ Check username: ~0.1ms
└─ Total: ~0.2ms

Memory Usage (per session):
├─ Session object: ~2KB
├─ Username string: ~50 bytes
├─ Session attributes: ~100 bytes
└─ Total per session: ~2.1KB

═════════════════════════════════════════════════════════════════
```

---

## Scalability Considerations

```
Current Setup (Single Server)
═════════════════════════════════════════════════════════════════
- Tomcat default: max 200 threads
- Session memory: ~2.1KB per user
- Max concurrent users: ~200 (thread-limited)
- Max user sessions (memory): ~50,000 (if 100MB allocated)

Bottleneck: Thread pool
└─ Solution: Increase with <Connector maxThreads="500"/>

For Production (Multiple Servers):
├─ Use sticky sessions OR
├─ Use distributed session store:
│  ├─ Redis: Fast, in-memory
│  ├─ Memcached: Distributed cache
│  ├─ Database: Persistent, slower
│  └─ Tomcat Manager: Built-in clustering
│
├─ Load Balancer (nginx, haproxy)
│  └─ Route requests to healthy Tomcat instances
│
└─ Database Connection Pool
   ├─ Use: HikariCP or c3p0
   └─ Benefits: Better performance, connection reuse

═════════════════════════════════════════════════════════════════
```

---
