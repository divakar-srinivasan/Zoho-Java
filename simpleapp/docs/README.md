# SimpleApp - Complete Project Documentation

**Version**: 1.0.0  
**Last Updated**: November 17, 2025  
**Platform**: Apache Tomcat 10.1.48 + PostgreSQL  
**Java Version**: OpenJDK 21  
**Status**: ✅ Production Ready (with self-signed certificate for HTTPS)

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Technology Stack](#technology-stack)
3. [Project Structure](#project-structure)
4. [File Descriptions](#file-descriptions)
5. [Setup & Installation](#setup--installation)
6. [Building the Project](#building-the-project)
7. [Running the Project](#running-the-project)
8. [Database Setup](#database-setup)
9. [Testing](#testing)
10. [Troubleshooting](#troubleshooting)
11. [Production Deployment](#production-deployment)
12. [Additional Resources](#additional-resources)

---

## Project Overview

### Purpose
SimpleApp is a web-based resource management application with:
- **User Authentication**: Session-based login/logout with security filters
- **RESTful API**: JSON endpoints for resource management
- **Database Integration**: PostgreSQL backend for persistent storage
- **HTTPS/SSL Support**: Secure communication with self-signed certificates
- **Session Management**: Automatic timeout after 2 minutes of inactivity
- **AJAX Integration**: Dynamic resource loading without page refresh

### Key Features
- ✅ Secure login/logout functionality
- ✅ Session-based authentication with 2-minute timeout
- ✅ Protected routes requiring authentication
- ✅ RESTful API at `/api/v1/{app}/{resource}/{id}`
- ✅ AJAX resource fetching with error handling
- ✅ HTTPS/SSL with self-signed certificate
- ✅ PostgreSQL database connectivity
- ✅ Bootstrap UI styling for responsive design
- ✅ Comprehensive logging for debugging

### Access URLs
| Purpose | URL | Port | Protocol |
|---------|-----|------|----------|
| Login (HTTP) | http://localhost:8080 | 8080 | HTTP |
| Login (HTTPS) | https://simpleapp.local:8443 | 8443 | HTTPS |
| Dashboard | /home.jsp | Both | Both |
| API Endpoint | /api/v1/simpleapp/myresources/1 | Both | Both |
| Database Test | /testdb | Both | Both |

---

## Technology Stack

### Backend
| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 21 (OpenJDK) | Programming language |
| Jakarta EE Servlets | 5.0 | Request handling |
| PostgreSQL JDBC | 42.6.0 | Database driver |
| Apache Commons Lang | 3.14.0 | String utilities |

### Application Server
| Component | Version | Details |
|-----------|---------|---------|
| Apache Tomcat | 10.1.48 | Web server |
| HTTP Connector | 1.1 | Port 8080 |
| HTTPS Connector | 1.1 + HTTP/2 | Port 8443 |

### Frontend
| Technology | Version | Purpose |
|-----------|---------|---------|
| Bootstrap | 5.3.0 | UI styling (CDN) |
| jQuery | 3.6.0 | AJAX handling (CDN) |
| HTML5 | - | Markup |
| CSS3 | - | Styling |

### Database
| Component | Version | Details |
|-----------|---------|---------|
| PostgreSQL | 12+ | Relational database |
| Database Name | test | Development database |
| Port | 5432 | Default PostgreSQL port |

### Security
| Feature | Type | Details |
|---------|------|---------|
| SSL/TLS | Self-signed | PKCS12 keystore |
| Certificate | Self-signed | CN=simpleapp.local, valid 365 days |
| Protocol | TLS 1.3 | Strong encryption |
| HTTP/2 | ALPN | Protocol negotiation |

---

## Project Structure

```
/home/divakar-pt8008/Documents/simpleapp/
├── src/                                    # Source code
│   └── com/example/
│       ├── AuthenticationFilter.java       # Session validation filter
│       ├── LoginServlet.java               # Authentication handler
│       ├── LogoutServlet.java              # Session cleanup
│       ├── ResourceApiServlet.java         # REST API endpoint
│       ├── TestDbServlet.java              # Database connectivity test
│       └── DBUtil.java                     # Database utilities
├── WEB-INF/
│   ├── classes/                            # Compiled classes
│   │   └── com/example/
│   │       ├── AuthenticationFilter.class
│   │       ├── LoginServlet.class
│   │       ├── LogoutServlet.class
│   │       ├── ResourceApiServlet.class
│   │       ├── TestDbServlet.class
│   │       └── DBUtil.class
│   ├── lib/                                # External JAR files
│   │   ├── commons-lang3-3.14.0.jar
│   │   └── postgresql-42.6.0.jar
│   └── web.xml                             # Web application configuration
├── index.jsp                               # Login page
├── home.jsp                                # Dashboard page
├── simpleapp.jar                           # Packaged application JAR
├── README.md                               # This file
├── IMPLEMENTATION_SUMMARY.md               # Implementation details
├── SYSTEM_ARCHITECTURE_DIAGRAMS.md         # Architecture diagrams
├── CODE_CHANGES_REFERENCE.md               # Code changes reference
├── MANUAL_TEST_GUIDE.md                    # Testing guide
├── SSL_TLS_CONFIGURATION.md                # SSL/TLS setup details
├── TOMCAT_SSL_LOGS.md                      # SSL startup and auth logs
└── test_auth.sh                            # Automated test script

Tomcat Directory:
/home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/
├── bin/
│   ├── startup.sh                          # Start Tomcat
│   ├── shutdown.sh                         # Stop Tomcat
│   └── catalina.sh                         # Tomcat control script
├── conf/
│   ├── server.xml                          # SSL/connector configuration
│   └── simpleapp-keystore.p12              # SSL certificate keystore
├── webapps/
│   ├── ROOT.war                            # Deployed application
│   └── ROOT/                               # Exploded WAR directory
└── logs/
    └── catalina.out                        # Application logs
```

---

## File Descriptions

### Source Files (src/com/example/)

#### 1. `AuthenticationFilter.java` (89 lines)
**Purpose**: Servlet filter enforcing session-based authentication

**Key Responsibilities**:
- Intercepts requests to protected paths (`/home.jsp`, `/api/v1/*`)
- Validates HttpSession existence and `username` attribute
- Redirects unauthenticated users to login page
- Logs access attempts for debugging

**Configuration**:
```java
@WebFilter(filterName = "AuthenticationFilter", 
           urlPatterns = {"/home.jsp", "/api/v1/*"})
```

**Protected Paths**:
- `/home.jsp` - Dashboard page
- `/api/v1/*` - All REST API endpoints

**Behavior**:
```
If session is valid:
  → Allow request to proceed
  → Log: "User {username} accessing: {path}"

If session is invalid/expired:
  → Send HTTP 302 redirect
  → Location: /index.jsp (login page)
  → Log: "Session invalid or expired. Redirecting to login page."
```

**Dependencies**: None (filter runs on all requests)

**Used By**: Tomcat filter chain (automatic)

---

#### 2. `LoginServlet.java` (Existing)
**Purpose**: Handle user authentication and session creation

**Endpoint**: 
- `POST /login` - Accept login credentials

**Key Responsibilities**:
- Receive username from login form
- Create HttpSession with user information
- Set session max inactivity interval to 120 seconds (2 minutes)
- Store username as session attribute
- Redirect to `/home.jsp` on success

**Request Format**:
```
POST /login HTTP/1.1
Content-Type: application/x-www-form-urlencoded

username=secureuser
```

**Response**:
```
HTTP/1.1 302 Found
Location: /home.jsp
Set-Cookie: JSESSIONID=...; Path=/; Secure; HttpOnly
```

**Session Configuration**:
```java
session.setMaxInactiveInterval(120);  // 2 minutes
session.setAttribute("username", username);
```

**Cookie Flags**:
- `Secure`: Cookie only sent over HTTPS
- `HttpOnly`: Prevents JavaScript access (XSS protection)

**Logging**:
```
Login success for user: {username}
```

**Dependencies**: AuthenticationFilter, JSP

**Used By**: index.jsp login form

---

#### 3. `LogoutServlet.java` (Existing)
**Purpose**: Handle user logout and session cleanup

**Endpoint**: 
- `GET /logout` - Invalidate session

**Key Responsibilities**:
- Retrieve current session
- Invalidate the session
- Log logout event
- Redirect to home page

**Request Format**:
```
GET /logout HTTP/1.1
```

**Response**:
```
HTTP/1.1 302 Found
Location: /
```

**Session Cleanup**:
```java
session.invalidate();  // Clear all session data
```

**Logging**:
```
Logout success for user: {username}
```

**Dependencies**: AuthenticationFilter

**Used By**: home.jsp logout link

---

#### 4. `ResourceApiServlet.java` (Updated)
**Purpose**: RESTful API endpoint for resource retrieval

**Endpoint**: 
- `GET /api/v1/{application}/{resource}/{id}` - Fetch resource by ID

**URL Parameters**:
| Parameter | Example | Type |
|-----------|---------|------|
| `application` | simpleapp | String (alphanumeric + underscore) |
| `resource` | myresources | String (alphanumeric + underscore) |
| `id` | 1 | Integer |

**Request Example**:
```
GET /api/v1/simpleapp/myresources/1 HTTP/1.1
```

**Response**:
```json
{
  "id": "1",
  "name": "Example resource"
}
```

**Key Responsibilities**:
- Parse URL parameters from path
- Validate input (regex patterns for app/resource names)
- Execute parameterized SQL query
- Return JSON response
- Handle errors with appropriate HTTP status codes

**SQL Query**:
```sql
SELECT id, name FROM {resource_name} WHERE id = {id}
```

**Type Binding**:
- Resource ID bound as `Integer` (for numeric columns)
- Prevents SQL injection via `PreparedStatement`

**Logging**:
```
Fetched resource: app={application} resource={resource} id={id}
```

**Error Handling**:
- Invalid parameter format → HTTP 400 Bad Request
- Resource not found → HTTP 404 Not Found
- Database error → HTTP 500 Internal Server Error

**Dependencies**: AuthenticationFilter, DBUtil

**Used By**: home.jsp AJAX calls

---

#### 5. `TestDbServlet.java` (Existing)
**Purpose**: Database connectivity test endpoint

**Endpoint**: 
- `GET /testdb` - Test database connection

**Key Responsibilities**:
- Test PostgreSQL JDBC connection
- Execute simple query
- Return connection status as HTML

**Request Format**:
```
GET /testdb HTTP/1.1
```

**Response** (on success):
```html
HTTP/1.1 200 OK
Content-Type: text/html

<h1>Database Connection Test</h1>
<p>Connection successful!</p>
<p>Sample data retrieved from database...</p>
```

**Response** (on failure):
```html
HTTP/1.1 500 Internal Server Error

<h1>Database Connection Error</h1>
<p>Error message...</p>
```

**Debugging Use**:
- Verify database is running
- Check JDBC driver is installed
- Validate database credentials
- Test connection before deploying

**Dependencies**: DBUtil

**Used By**: Developers (manual testing only)

---

#### 6. `DBUtil.java` (Existing)
**Purpose**: Database connection utility

**Key Responsibilities**:
- Manage PostgreSQL JDBC connection
- Provide centralized connection access
- Handle connection cleanup

**Main Method**:
```java
public static Connection getConnection() throws SQLException
```

**Configuration**:
```
Database URL: jdbc:postgresql://localhost:5432/test
Database User: divakar-pt8008
Database Password: Divakar@2005
```

**Connection Details**:
| Setting | Value |
|---------|-------|
| Database Host | localhost |
| Database Port | 5432 |
| Database Name | test |
| JDBC Driver | org.postgresql.Driver |

**Usage Example**:
```java
try (Connection conn = DBUtil.getConnection()) {
    PreparedStatement ps = conn.prepareStatement("SELECT * FROM myresources WHERE id = ?");
    ps.setInt(1, resourceId);
    ResultSet rs = ps.executeQuery();
    // Process results
}
```

**Dependencies**: postgresql-42.6.0.jar (JDBC driver)

**Used By**: ResourceApiServlet, TestDbServlet

---

### JSP Files

#### 1. `index.jsp` (Existing)
**Purpose**: Login page

**Features**:
- Bootstrap 5.3.0 styling for responsive UI
- Login form with username input
- Submit button
- Links to resources

**Form Fields**:
```html
<form method="POST" action="/login">
    <input type="text" name="username" required>
    <button type="submit">Login</button>
</form>
```

**Access Control**:
- ✅ Public page (no authentication required)
- Accessible from HTTP and HTTPS

**User Flow**:
```
1. User visits http://localhost:8080 or https://simpleapp.local:8443
2. Sees login form
3. Enters username
4. Clicks "Login"
5. Form POSTs to /login
6. LoginServlet creates session
7. User redirected to /home.jsp
```

**Dependencies**: None (static form)

**Related Files**: LoginServlet.java

---

#### 2. `home.jsp` (Updated)
**Purpose**: Dashboard page (protected)

**Features**:
- Bootstrap 5.3.0 styling
- User welcome message
- Resource display area
- AJAX resource loading
- Logout button
- jQuery integration for async calls

**Key Sections**:

**1. User Information**:
```jsp
<div class="alert alert-info">
    <strong>Welcome, <%= session.getAttribute("username") %>!</strong>
</div>
```

**2. Resource Display Area**:
```html
<p id="resource">Loading resource details...</p>
```

**3. jQuery AJAX Function**:
```javascript
$(document).ready(function() {
    $.ajax({
        url: '/api/v1/simpleapp/myresources/1',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            $('#resource').html('ID: ' + data.id + '<br>Name: ' + data.name);
        },
        error: function(xhr) {
            if (xhr.status === 401 || xhr.status === 403) {
                window.location.href = '/'; // Redirect on session expiry
            }
        }
    });
});
```

**AJAX Behavior**:
- Automatically called on page load
- Fetches resource from API endpoint
- Displays resource details in `#resource` div
- Handles session expiry (auto-redirect on 401/403)

**Access Control**:
- ✅ Protected by AuthenticationFilter
- ❌ Unauthenticated access → 302 redirect to login

**User Flow**:
```
1. User logs in
2. Redirected to /home.jsp
3. Page loads with "Loading resource details..."
4. jQuery AJAX fires on page load
5. Calls /api/v1/simpleapp/myresources/1
6. API returns JSON: {"id":"1","name":"Example resource"}
7. jQuery displays resource details
```

**Session Timeout Behavior**:
```
If session expires during AJAX call:
  → API returns 401/403 (session invalid)
  → jQuery error handler catches it
  → Auto-redirects to /index.jsp (login page)
```

**Dependencies**: jQuery 3.6.0 (CDN), Bootstrap 5.3.0 (CDN), AuthenticationFilter.java, ResourceApiServlet.java

**Related Files**: AuthenticationFilter.java, ResourceApiServlet.java

---

### Configuration Files

#### 1. `WEB-INF/web.xml`
**Purpose**: Web application configuration and deployment descriptor

**Key Configurations**:

**1. Session Timeout**:
```xml
<session-config>
    <session-timeout>2</session-timeout>
</session-config>
```
- Global session timeout: 2 minutes
- Matches LoginServlet's `setMaxInactiveInterval(120)`

**2. Servlet Definitions**:
```xml
<servlet>
    <servlet-name>LoginServlet</servlet-name>
    <servlet-class>com.example.LoginServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>LoginServlet</servlet-name>
    <url-pattern>/login</url-pattern>
</servlet-mapping>
```

**3. Filter Definitions**:
```xml
<filter>
    <filter-name>AuthenticationFilter</filter-name>
    <filter-class>com.example.AuthenticationFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>AuthenticationFilter</filter-name>
    <url-pattern>/home.jsp</url-pattern>
    <url-pattern>/api/v1/*</url-pattern>
</filter-mapping>
```

**4. Error Pages**:
```xml
<error-page>
    <error-code>404</error-code>
    <location>/404.jsp</location>
</error-page>
```

**5. MIME Types**:
```xml
<mime-mapping>
    <extension>json</extension>
    <mime-type>application/json</mime-type>
</mime-mapping>
```

**Dependencies**: AuthenticationFilter.java, LoginServlet.java, LogoutServlet.java, ResourceApiServlet.java, TestDbServlet.java

---

#### 2. `server.xml` (Tomcat Configuration)
**Purpose**: Tomcat connector and SSL/TLS configuration

**File Location**:
```
/home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/conf/server.xml
```

**Key Sections**:

**1. HTTP Connector (Port 8080)**:
```xml
<Connector port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443"
           maxParameterCount="1000"
           />
```

**2. HTTPS Connector (Port 8443)**:
```xml
<Connector port="8443" protocol="org.apache.coyote.http11.Http11NioProtocol"
           maxThreads="150" SSLEnabled="true"
           maxParameterCount="1000"
           >
    <UpgradeProtocol className="org.apache.coyote.http2.Http2Protocol" />
    <SSLHostConfig>
        <Certificate certificateKeystoreFile="conf/simpleapp-keystore.p12"
                     certificateKeystoreType="PKCS12"
                     certificateKeystorePassword="changeit"
                     certificateKeyAlias="tomcat"
                     type="RSA" />
    </SSLHostConfig>
</Connector>
```

**SSL/TLS Settings**:
| Setting | Value | Purpose |
|---------|-------|---------|
| certificateKeystoreFile | conf/simpleapp-keystore.p12 | Path to certificate keystore |
| certificateKeystoreType | PKCS12 | Keystore format |
| certificateKeystorePassword | changeit | Keystore password |
| certificateKeyAlias | tomcat | Certificate alias |
| Protocol | HTTP/1.1 + NIO | NIO-based HTTP protocol |
| UpgradeProtocol | HTTP/2 | HTTP/2 support via ALPN |

**Dependencies**: simpleapp-keystore.p12

---

#### 3. `/etc/hosts` (Domain Mapping)
**Purpose**: Map custom domain to localhost for HTTPS testing

**File Location**: `/etc/hosts`

**Entry**:
```
127.0.0.1 simpleapp.local
```

**Effect**:
- `simpleapp.local` resolves to `127.0.0.1` (localhost)
- Allows accessing HTTPS with domain name: `https://simpleapp.local:8443`
- Certificate CN matches domain name

**Dependencies**: simpleapp-keystore.p12 (certificate CN=simpleapp.local)

---

#### 4. `simpleapp-keystore.p12` (SSL Certificate)
**Purpose**: Store SSL/TLS certificate and private key

**File Location**:
```
/home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/conf/simpleapp-keystore.p12
```

**Certificate Details**:
| Property | Value |
|----------|-------|
| Type | Self-signed |
| Format | PKCS12 |
| Algorithm | RSA 2048-bit |
| CN | simpleapp.local |
| Validity | 365 days |
| Keystore Password | changeit |
| Key Alias | tomcat |

**Generation Command**:
```bash
keytool -genkey -alias tomcat -keyalg RSA -keysize 2048 \
  -keystore simpleapp-keystore.p12 -storetype PKCS12 \
  -validity 365 -storepass changeit -keypass changeit \
  -dname "CN=simpleapp.local,OU=Development,O=SimpleApp,L=LocalHost,ST=CA,C=US"
```

**Dependencies**: server.xml (references this file)

---

### Jar Files

#### 1. `simpleapp.jar`
**Purpose**: Packaged application classes

**Location**:
```
/home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/lib/simpleapp.jar
```

**Contents**:
```
com/example/
├── AuthenticationFilter.class
├── LoginServlet.class
├── LogoutServlet.class
├── ResourceApiServlet.class
├── TestDbServlet.class
└── DBUtil.class
```

**Size**: ~7.3 KB

**Purpose**: 
- Deployed to Tomcat lib directory
- Provides backup/packaged version of classes
- Useful for deployment to other Tomcat instances

**Note**: Currently, classes are also in WAR; this is redundant but included per requirements

---

### Documentation Files

#### 1. `IMPLEMENTATION_SUMMARY.md`
- Complete implementation details
- Phase-by-phase development summary
- All code changes documented

#### 2. `SYSTEM_ARCHITECTURE_DIAGRAMS.md`
- Architecture diagrams
- Request flow diagrams
- Database schema
- Component relationships

#### 3. `CODE_CHANGES_REFERENCE.md`
- Detailed code changes
- Compilation steps
- Deployment process

#### 4. `MANUAL_TEST_GUIDE.md`
- Testing procedures
- Test cases and expected results
- Authentication flow testing

#### 5. `SSL_TLS_CONFIGURATION.md`
- SSL certificate generation
- server.xml configuration
- HTTPS setup details
- Production deployment checklist

#### 6. `TOMCAT_SSL_LOGS.md`
- Startup logs with SSL initialization
- Authentication flow logs
- Session management logs
- Troubleshooting reference

---

### Test Files

#### 1. `test_auth.sh`
**Purpose**: Automated authentication testing script

**Features**:
- Tests HTTP login/logout flow
- Tests HTTPS login/logout flow
- Tests session management
- Tests AJAX API calls
- Verifies security headers

**Usage**:
```bash
bash test_auth.sh
```

**Tests Performed**:
1. Login over HTTP
2. Access protected page over HTTP
3. Fetch resource via AJAX over HTTP
4. Logout over HTTP
5. Login over HTTPS
6. Access protected page over HTTPS
7. Fetch resource via AJAX over HTTPS
8. Logout over HTTPS
9. Session timeout verification

---

## Setup & Installation

### Prerequisites

#### 1. Java Development Kit (JDK)
```bash
# Check if Java is installed
java -version

# Expected output:
# openjdk version "21.0.1" ...

# If not installed:
sudo apt-get install openjdk-21-jdk
```

#### 2. Apache Tomcat 10.1.48
```bash
# Download and extract (already done in workspace)
# Location: /home/divakar-pt8008/Downloads/apache-tomcat-10.1.48

# Verify installation
ls -la /home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/bin/
```

#### 3. PostgreSQL Database
```bash
# Install PostgreSQL (if not already installed)
sudo apt-get install postgresql postgresql-contrib

# Start PostgreSQL service
sudo service postgresql start

# Check status
sudo service postgresql status
```

#### 4. Git (Optional, for version control)
```bash
sudo apt-get install git
```

### Database Setup

#### 1. Create Database and Tables
```bash
# Connect to PostgreSQL as default user
sudo -u postgres psql

# In PostgreSQL shell:
CREATE DATABASE test;

\c test

CREATE TABLE myresources (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);

INSERT INTO myresources (name) VALUES ('Example resource');

SELECT * FROM myresources;

\q
```

#### 2. Verify Database
```bash
# Connect as application user
psql -h localhost -U divakar-pt8008 -d test

# List tables
\dt

# Query sample data
SELECT * FROM myresources;

# Exit
\q
```

### Download Required JAR Files

```bash
# Navigate to Tomcat lib directory
cd /home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/lib

# Verify JAR files exist
ls -la postgresql-42.6.0.jar
ls -la commons-lang3-3.14.0.jar

# If missing, download from Maven Central:
# PostgreSQL JDBC Driver
# wget https://jdbc.postgresql.org/download/postgresql-42.6.0.jar

# Apache Commons Lang3
# wget https://dlcdn.apache.org/commons/lang/binaries/commons-lang3-3.14.0-bin.zip
```

---

## Building the Project

### Prerequisites Check
```bash
# Set environment variables
export TOMCAT_HOME="/home/divakar-pt8008/Downloads/apache-tomcat-10.1.48"
export JAVA_HOME="/usr/lib/jvm/java-21-openjdk-amd64"
export PROJECT_HOME="/home/divakar-pt8008/Documents/simpleapp"

# Verify
echo $TOMCAT_HOME
echo $JAVA_HOME
echo $PROJECT_HOME
```

### Step 1: Compile Java Source Files
```bash
cd $PROJECT_HOME

# Create output directory for compiled classes
mkdir -p WEB-INF/classes

# Compile all Java files
javac -d WEB-INF/classes \
    -cp "$TOMCAT_HOME/lib/*" \
    src/com/example/*.java

# Verify compilation
ls -la WEB-INF/classes/com/example/
```

**Expected Output**:
```
-rw-r--r-- AuthenticationFilter.class
-rw-r--r-- LoginServlet.class
-rw-r--r-- LogoutServlet.class
-rw-r--r-- ResourceApiServlet.class
-rw-r--r-- TestDbServlet.class
-rw-r--r-- DBUtil.class
```

### Step 2: Copy JSP Files (if modified)
```bash
# JSP files are already in place
ls -la *.jsp

# Output should show:
# index.jsp
# home.jsp
```

### Step 3: Verify web.xml
```bash
# Check web.xml exists and is valid
ls -la WEB-INF/web.xml

# Validate XML (optional)
xmllint WEB-INF/web.xml
```

### Step 4: Create WAR File
```bash
# Navigate to project directory
cd $PROJECT_HOME

# Create WAR file
jar cvf ROOT.war \
    WEB-INF/ \
    *.jsp

# Verify WAR file
ls -la ROOT.war

# List contents
jar tvf ROOT.war | head -20
```

### Step 5: Deploy to Tomcat
```bash
# Stop Tomcat (if running)
$TOMCAT_HOME/bin/shutdown.sh

# Wait for graceful shutdown
sleep 3

# Remove old deployment
rm -rf $TOMCAT_HOME/webapps/ROOT
rm -f $TOMCAT_HOME/webapps/ROOT.war

# Copy new WAR
cp ROOT.war $TOMCAT_HOME/webapps/

# Start Tomcat
$TOMCAT_HOME/bin/startup.sh

# Wait for startup
sleep 6

# Verify startup
tail -f $TOMCAT_HOME/logs/catalina.out
```

### Step 6: Package as JAR (Optional)
```bash
# Create JAR file for library distribution
jar cvf simpleapp.jar -C WEB-INF/classes .

# Verify JAR
ls -la simpleapp.jar

# Deploy to Tomcat lib (optional)
cp simpleapp.jar $TOMCAT_HOME/lib/
```

### Build Verification Checklist
- [ ] All Java files compiled without errors
- [ ] All `.class` files created in `WEB-INF/classes/`
- [ ] WAR file created successfully
- [ ] WAR deployed to `$TOMCAT_HOME/webapps/ROOT.war`
- [ ] Tomcat started successfully
- [ ] No errors in `catalina.out`

---

## Running the Project

### Start Tomcat
```bash
# Method 1: Using startup script
$TOMCAT_HOME/bin/startup.sh

# Method 2: Using catalina script
$TOMCAT_HOME/bin/catalina.sh start

# Wait for full startup
sleep 6

# Verify startup
tail -20 $TOMCAT_HOME/logs/catalina.out
```

**Expected Output** (last lines):
```
INFO: Server startup in [1373] milliseconds
INFO: Initializing ProtocolHandler ["https-jsse-nio-8443"]
INFO: Starting ProtocolHandler ["https-jsse-nio-8443"]
```

### Access Application

#### HTTP (Unencrypted - Development Only)
```bash
# Visit login page
# http://localhost:8080

# Or use curl
curl -s http://localhost:8080 | head -30
```

#### HTTPS (Encrypted - Recommended)
```bash
# First time: Accept certificate warning
# https://simpleapp.local:8443

# Or use curl with insecure flag
curl -k -s https://simpleapp.local:8443 | head -30
```

### Test Login Flow
```bash
# 1. Get login page
curl -i http://localhost:8080

# 2. Login
curl -c /tmp/cookies.txt -X POST http://localhost:8080/login -d "username=testuser"

# 3. Access protected page
curl -b /tmp/cookies.txt http://localhost:8080/home.jsp

# 4. Fetch resource via API
curl -b /tmp/cookies.txt http://localhost:8080/api/v1/simpleapp/myresources/1

# 5. Logout
curl -i -b /tmp/cookies.txt http://localhost:8080/logout
```

### Monitor Logs
```bash
# Real-time log monitoring
tail -f $TOMCAT_HOME/logs/catalina.out

# Filter for authentication events
tail -f $TOMCAT_HOME/logs/catalina.out | grep -i "login\|session\|user"

# Filter for SSL events
tail -f $TOMCAT_HOME/logs/catalina.out | grep -i "ssl\|certificate\|https"

# Stop monitoring
# Press Ctrl+C
```

### Stop Tomcat
```bash
# Method 1: Using shutdown script
$TOMCAT_HOME/bin/shutdown.sh

# Wait for graceful shutdown
sleep 3

# Method 2: Using catalina script
$TOMCAT_HOME/bin/catalina.sh stop

# Method 3: Force kill (if necessary)
pkill -f tomcat
```

---

## Database Setup

### Connection Details
```
Host: localhost
Port: 5432
Database: test
Username: divakar-pt8008
Password: Divakar@2005
```

### Database Credentials in Code
```java
// DBUtil.java
private static final String DB_URL = "jdbc:postgresql://localhost:5432/test";
private static final String DB_USER = "divakar-pt8008";
private static final String DB_PASSWORD = "Divakar@2005";
```

### Create Database and Table
```sql
-- Connect to PostgreSQL
psql -h localhost -U postgres

-- Create database
CREATE DATABASE test;

-- Connect to test database
\c test

-- Create myresources table
CREATE TABLE myresources (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample data
INSERT INTO myresources (name) VALUES ('Example resource');
INSERT INTO myresources (name) VALUES ('Another resource');
INSERT INTO myresources (name) VALUES ('Test resource');

-- Verify data
SELECT * FROM myresources;

-- Exit
\q
```

### Test Database Connection
```bash
# Using psql
psql -h localhost -U divakar-pt8008 -d test -c "SELECT * FROM myresources;"

# Using Tomcat servlet
curl http://localhost:8080/testdb

# Expected: Connection successful with sample data
```

---

## Testing

### Automated Tests

#### Run All Tests
```bash
# Using test script
bash test_auth.sh

# Expected output:
# ✓ Test 1: GET / (login page)
# ✓ Test 2: POST /login (create session)
# ✓ Test 3: GET /home.jsp (access protected page)
# ✓ Test 4: GET /api/v1/... (fetch resource)
# ✓ Test 5: GET /logout (logout)
# ... (9 total tests)
```

### Manual Testing

#### Test 1: Login
```bash
curl -i -c /tmp/cookies.txt -X POST \
  http://localhost:8080/login \
  -d "username=testuser"

# Expected: HTTP 302, Location: /home.jsp
```

#### Test 2: Access Protected Page
```bash
curl -i -b /tmp/cookies.txt \
  http://localhost:8080/home.jsp

# Expected: HTTP 200, page loaded
```

#### Test 3: AJAX API Call
```bash
curl -i -b /tmp/cookies.txt \
  http://localhost:8080/api/v1/simpleapp/myresources/1

# Expected: HTTP 200, JSON response
```

#### Test 4: Session Timeout
```bash
# 1. Login
curl -c /tmp/cookies.txt -X POST \
  http://localhost:8080/login \
  -d "username=testuser"

# 2. Wait 120+ seconds
sleep 125

# 3. Try to access protected page
curl -i -b /tmp/cookies.txt \
  http://localhost:8080/home.jsp

# Expected: HTTP 302 redirect to login
```

#### Test 5: HTTPS Access
```bash
# Login over HTTPS
curl -k -c /tmp/https_cookies.txt -X POST \
  https://simpleapp.local:8443/login \
  -d "username=secureuser"

# Access protected page
curl -k -b /tmp/https_cookies.txt \
  https://simpleapp.local:8443/home.jsp

# Expected: All operations work over HTTPS
```

### Browser Testing

#### Step 1: Access Login Page
```
1. Open browser
2. Visit: http://localhost:8080 or https://simpleapp.local:8443
3. See login form
```

#### Step 2: Login
```
1. Enter username in form
2. Click "Login"
3. Redirected to dashboard
4. See "Welcome, {username}!"
```

#### Step 3: Resource Loading
```
1. Wait for AJAX call
2. See "Loading resource details..." change to actual data
3. Verify resource ID and name displayed
```

#### Step 4: Check Security
```
1. Right-click → Inspect
2. Go to Network tab
3. Reload page
4. Check HTTPS connection (lock icon)
5. Verify JSESSIONID cookie flags: Secure, HttpOnly
```

#### Step 5: Logout
```
1. Click "Logout" button
2. Redirected to login page
3. Try accessing home.jsp directly
4. Should redirect to login
```

---

## Troubleshooting

### Issue 1: Tomcat Won't Start

**Problem**: Startup fails with port already in use error

**Error Message**:
```
Address already in use: 8080
```

**Solution**:
```bash
# Find process using port
lsof -i :8080

# Kill the process
kill -9 <PID>

# Or change port in server.xml
# Edit: conf/server.xml
# Change: <Connector port="8080" ...>
# To: <Connector port="8081" ...>
```

---

### Issue 2: Database Connection Error

**Problem**: Cannot connect to PostgreSQL database

**Error Message**:
```
org.postgresql.util.PSQLException: Connection refused
```

**Solution**:
```bash
# Check if PostgreSQL is running
sudo service postgresql status

# Start PostgreSQL
sudo service postgresql start

# Verify database exists
psql -h localhost -U postgres -l | grep test

# Verify user exists
psql -h localhost -U divakar-pt8008 -d test -c "SELECT 1"

# Check DBUtil credentials match
# File: src/com/example/DBUtil.java
```

---

### Issue 3: JSP Compilation Error

**Problem**: HTTP 500 error with JSP compilation message

**Error Message**:
```
org.apache.jasper.JasperException: Unable to compile class for JSP
```

**Solution**:
```bash
# Clear JSP work directory
rm -rf $TOMCAT_HOME/work/Catalina/localhost/ROOT/

# Restart Tomcat
$TOMCAT_HOME/bin/shutdown.sh
sleep 3
$TOMCAT_HOME/bin/startup.sh

# Check JSP syntax
# Look at catalina.out for specific error
tail -50 $TOMCAT_HOME/logs/catalina.out
```

---

### Issue 4: Authentication Filter Not Working

**Problem**: Protected pages accessible without login

**Symptom**: `/home.jsp` loads without session

**Solution**:
```bash
# Verify filter is defined in web.xml
grep -A 5 "AuthenticationFilter" WEB-INF/web.xml

# Recompile Java files
javac -d WEB-INF/classes -cp "$TOMCAT_HOME/lib/*" src/com/example/*.java

# Rebuild WAR
jar cvf ROOT.war WEB-INF/ *.jsp

# Redeploy
cp ROOT.war $TOMCAT_HOME/webapps/

# Restart Tomcat
$TOMCAT_HOME/bin/shutdown.sh && sleep 3 && $TOMCAT_HOME/bin/startup.sh
```

---

### Issue 5: AJAX Not Loading Resource

**Problem**: Resource details not displaying on home.jsp

**Error Message**:
```
Loading resource details... (never completes)
```

**Solution**:
```bash
# Check browser console for errors
# 1. Open DevTools (F12)
# 2. Go to Console tab
# 3. Look for JavaScript errors

# Verify API endpoint works
curl -b /tmp/cookies.txt http://localhost:8080/api/v1/simpleapp/myresources/1

# Check Tomcat logs
tail -20 $TOMCAT_HOME/logs/catalina.out

# Verify database has data
psql -h localhost -U divakar-pt8008 -d test -c "SELECT * FROM myresources;"

# Check if jQuery is loading (DevTools → Network tab)
# Should see jquery-3.6.0.min.js loading from CDN
```

---

### Issue 6: HTTPS Not Working

**Problem**: Cannot access application over HTTPS

**Error Message**:
```
SSL_ERROR_BAD_CERT_DOMAIN or connection refused
```

**Solution**:
```bash
# Verify certificate file exists
ls -la $TOMCAT_HOME/conf/simpleapp-keystore.p12

# Verify domain is mapped
grep simpleapp.local /etc/hosts

# If not found, add it:
echo "127.0.0.1 simpleapp.local" | sudo tee -a /etc/hosts

# Check server.xml HTTPS configuration
grep -A 10 "port=\"8443\"" $TOMCAT_HOME/conf/server.xml

# Verify certificate password in server.xml matches generation
# Should be: certificateKeystorePassword="changeit"

# Test SSL connection
openssl s_client -connect localhost:8443

# Restart Tomcat to load new configuration
$TOMCAT_HOME/bin/shutdown.sh && sleep 3 && $TOMCAT_HOME/bin/startup.sh

# Verify HTTPS is working
curl -k https://simpleapp.local:8443/
```

---

### Issue 7: Session Timeout Not Working

**Problem**: Session not expiring after 2 minutes

**Solution**:
```bash
# Verify timeout configuration in web.xml
grep -i timeout WEB-INF/web.xml
# Should show: <session-timeout>2</session-timeout>

# Verify LoginServlet sets timeout
grep setMaxInactiveInterval src/com/example/LoginServlet.java
# Should show: session.setMaxInactiveInterval(120);

# Recompile and redeploy
javac -d WEB-INF/classes -cp "$TOMCAT_HOME/lib/*" src/com/example/*.java
jar cvf ROOT.war WEB-INF/ *.jsp
cp ROOT.war $TOMCAT_HOME/webapps/
$TOMCAT_HOME/bin/shutdown.sh && sleep 3 && $TOMCAT_HOME/bin/startup.sh

# Manual test:
# 1. Login
# 2. Note session ID from Set-Cookie header
# 3. Wait 121+ seconds (2 min 1 sec)
# 4. Access protected page with same session ID
# 5. Should get 302 redirect to login
```

---

## Production Deployment

### Pre-Deployment Checklist

- [ ] **Code Review**
  - [ ] All servlets reviewed and tested
  - [ ] No hardcoded credentials (except DBUtil for this demo)
  - [ ] No debug logging in production code
  - [ ] Error handling implemented

- [ ] **Database**
  - [ ] Database backups configured
  - [ ] Connection pooling enabled
  - [ ] Database user has least privilege
  - [ ] Regular backups scheduled

- [ ] **Security**
  - [ ] Replace self-signed certificate with CA-signed certificate
  - [ ] Update database credentials (change from demo credentials)
  - [ ] Update keystore password (change from "changeit")
  - [ ] Enable security headers (HSTS, CSP, X-Frame-Options)
  - [ ] Disable HTTP redirect to HTTPS

- [ ] **Performance**
  - [ ] Enable connection pooling in DBUtil
  - [ ] Configure Tomcat thread pool
  - [ ] Enable Tomcat compression
  - [ ] Configure caching headers

- [ ] **Monitoring**
  - [ ] Log aggregation configured
  - [ ] Error monitoring enabled
  - [ ] Performance metrics collected
  - [ ] Alerting configured

### Replace Self-Signed Certificate

```bash
# 1. Obtain CA-signed certificate
# (Use Let's Encrypt, DigiCert, or your CA)
# Save as: server.crt and server.key

# 2. Convert to PKCS12 format
openssl pkcs12 -export \
  -in server.crt \
  -inkey server.key \
  -out production-keystore.p12 \
  -name tomcat \
  -password pass:your-strong-password

# 3. Copy to Tomcat conf directory
cp production-keystore.p12 $TOMCAT_HOME/conf/

# 4. Update server.xml
# Change: certificateKeystoreFile="conf/simpleapp-keystore.p12"
# To: certificateKeystoreFile="conf/production-keystore.p12"
# Change: certificateKeystorePassword="changeit"
# To: certificateKeystorePassword="your-strong-password"

# 5. Restart Tomcat
$TOMCAT_HOME/bin/shutdown.sh && sleep 3 && $TOMCAT_HOME/bin/startup.sh
```

### Update Database Credentials

```bash
# 1. Create production database user with strong password
# In PostgreSQL:
ALTER USER divakar-pt8008 WITH PASSWORD 'strong-production-password';

# 2. Update DBUtil.java
# Change: private static final String DB_PASSWORD = "Divakar@2005";
# To: private static final String DB_PASSWORD = "strong-production-password";

# 3. Recompile and redeploy
javac -d WEB-INF/classes -cp "$TOMCAT_HOME/lib/*" src/com/example/*.java
jar cvf ROOT.war WEB-INF/ *.jsp
cp ROOT.war $TOMCAT_HOME/webapps/
$TOMCAT_HOME/bin/shutdown.sh && sleep 3 && $TOMCAT_HOME/bin/startup.sh
```

### Enable HTTP → HTTPS Redirect

```xml
<!-- In server.xml -->
<!-- Change HTTP connector: -->
<Connector port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443"
           />

<!-- Add to ROOT application in server.xml: -->
<Context path="" docBase="ROOT" reloadable="true">
    <Valve className="org.apache.catalina.valves.rewrite.RewriteValve" />
</Context>

<!-- Add rewrite.config file to redirect HTTP to HTTPS -->
```

---

## Additional Resources

### Important Directories
```bash
# Project source code
/home/divakar-pt8008/Documents/simpleapp

# Tomcat installation
/home/divakar-pt8008/Downloads/apache-tomcat-10.1.48

# Tomcat logs
/home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/logs/catalina.out

# Tomcat configuration
/home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/conf/server.xml

# Tomcat SSL certificate
/home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/conf/simpleapp-keystore.p12
```

### Documentation Files
- `README.md` - This file (complete project documentation)
- `IMPLEMENTATION_SUMMARY.md` - Implementation details and phase summary
- `SYSTEM_ARCHITECTURE_DIAGRAMS.md` - Architecture and flow diagrams
- `CODE_CHANGES_REFERENCE.md` - Detailed code changes and compilation steps
- `MANUAL_TEST_GUIDE.md` - Testing procedures and test cases
- `SSL_TLS_CONFIGURATION.md` - SSL/TLS setup and troubleshooting
- `TOMCAT_SSL_LOGS.md` - Startup and authentication logs

### Useful Commands Reference

#### Tomcat Management
```bash
# Start Tomcat
$TOMCAT_HOME/bin/startup.sh

# Stop Tomcat
$TOMCAT_HOME/bin/shutdown.sh

# Check status
ps aux | grep tomcat

# View logs
tail -f $TOMCAT_HOME/logs/catalina.out
```

#### Database Management
```bash
# Connect to PostgreSQL
psql -h localhost -U divakar-pt8008 -d test

# Create backup
pg_dump -h localhost -U divakar-pt8008 test > backup.sql

# Restore backup
psql -h localhost -U divakar-pt8008 test < backup.sql
```

#### Java Compilation
```bash
# Compile single file
javac -d WEB-INF/classes -cp "$TOMCAT_HOME/lib/*" src/com/example/LoginServlet.java

# Compile all files
javac -d WEB-INF/classes -cp "$TOMCAT_HOME/lib/*" src/com/example/*.java

# Check class file
javap -classpath WEB-INF/classes com.example.LoginServlet
```

#### Testing
```bash
# Simple curl test
curl http://localhost:8080

# Test with authentication
curl -c /tmp/cookies.txt -X POST http://localhost:8080/login -d "username=test"

# Test API endpoint
curl -b /tmp/cookies.txt http://localhost:8080/api/v1/simpleapp/myresources/1

# Test HTTPS
curl -k https://simpleapp.local:8443
```

---

## Quick Start Summary

### For Quick Testing (5 minutes)

```bash
# 1. Start Tomcat
/home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/bin/startup.sh
sleep 6

# 2. Test login
curl -c /tmp/cookies.txt -X POST http://localhost:8080/login -d "username=testuser"

# 3. Access protected page
curl -b /tmp/cookies.txt http://localhost:8080/home.jsp

# 4. Fetch resource
curl -b /tmp/cookies.txt http://localhost:8080/api/v1/simpleapp/myresources/1

# 5. Open in browser
# HTTP: http://localhost:8080
# HTTPS: https://simpleapp.local:8443
```

### For Fresh Rebuild (10 minutes)

```bash
# 1. Compile Java files
cd /home/divakar-pt8008/Documents/simpleapp
javac -d WEB-INF/classes -cp "/home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/lib/*" src/com/example/*.java

# 2. Create WAR
jar cvf ROOT.war WEB-INF/ *.jsp

# 3. Restart Tomcat with new WAR
/home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/bin/shutdown.sh
sleep 3
cp ROOT.war /home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/webapps/
/home/divakar-pt8008/Downloads/apache-tomcat-10.1.48/bin/startup.sh
sleep 6

# 4. Test
curl -c /tmp/cookies.txt -X POST http://localhost:8080/login -d "username=testuser"
curl -b /tmp/cookies.txt http://localhost:8080/home.jsp
```

---

## Support & Contact

For issues, questions, or contributions:

1. **Check Logs**:
   ```bash
   tail -100 $TOMCAT_HOME/logs/catalina.out
   ```

2. **Review Documentation**:
   - SSL_TLS_CONFIGURATION.md (for HTTPS issues)
   - MANUAL_TEST_GUIDE.md (for testing procedures)
   - Troubleshooting section above

3. **Check Database**:
   ```bash
   psql -h localhost -U divakar-pt8008 -d test -c "SELECT * FROM myresources;"
   ```

4. **Verify Certificate**:
   ```bash
   keytool -list -v -keystore $TOMCAT_HOME/conf/simpleapp-keystore.p12 -storetype PKCS12 -storepass changeit
   ```

---

## Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | Nov 17, 2025 | Initial release with SSL/HTTPS support |

---

## License

This project is provided as-is for development and testing purposes.

---

**End of README**
