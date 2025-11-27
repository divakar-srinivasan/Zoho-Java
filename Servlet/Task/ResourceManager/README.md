# ResourceManager Application Configuration

## Project Structure
```
ResourceManager/
├── pom.xml                          # Maven configuration
├── database-setup.sql               # Database initialization script
├── setup-db.sh                      # Database setup shell script
├── build.sh                         # Build script
├── create-ssl-certificate.sh        # SSL certificate generation script
├── deploy-to-tomcat.sh              # Deployment script
├── ssl-connector-config.xml         # Tomcat SSL configuration
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/resourcemanager/
│   │   │       ├── servlet/
│   │   │       │   ├── LoginServlet.java
│   │   │       │   ├── LogoutServlet.java
│   │   │       │   └── ResourceServlet.java
│   │   │       ├── filter/
│   │   │       │   └── AuthenticationFilter.java
│   │   │       ├── dao/
│   │   │       │   ├── UserDAO.java
│   │   │       │   └── ResourceDAO.java
│   │   │       └── util/
│   │   │           ├── DatabaseConnection.java
│   │   │           └── Logger.java
│   │   └── webapp/
│   │       ├── login.jsp
│   │       ├── home.jsp
│   │       ├── index.jsp
│   │       ├── css/
│   │       │   └── style.css
│   │       ├── js/
│   │       └── WEB-INF/
│   │           └── web.xml
│   └── test/
└── target/
    └── ROOT.war

## Features Implemented

### 1. Login Page
- Username input field
- Password input field
- Submit button with validation
- Error message display
- Responsive design

### 2. Home Page
- Top Navigation Bar
  - Left: Application title and navigation menu
  - Right: User profile with avatar and dropdown menu
- Sidebar with navigation menu
- Main content area
- Dashboard view
- Resources view with AJAX data loading
- Settings view

### 3. API Endpoint
- Endpoint: `/api/v1/resourcemanager/{resource-name}/{resource-id}`
- Returns JSON with resource ID and name
- GET method implementation
- POST method for resource creation
- Error handling with proper HTTP status codes
- Logging for all API calls

### 4. Authentication Filter
- Filters all requests
- Checks for valid session
- Redirects to login page if session is invalid/expired
- Public path handling (login, CSS, JS)
- Session validation on each request

### 5. Session Management
- Session creation on successful login
- Session timeout set to 5 minutes
- Session invalidation on logout
- Session attributes: username, userId

### 6. Logging
- Login success logging
- Login failure logging
- Resource API access logging
- Exception logging
- Logout logging
- All logs written to `/tmp/resourcemanager.log`

### 7. Database Integration
- PostgreSQL connection
- User validation
- Resource retrieval
- Connection pooling via JDBC

### 8. SSL/TLS Configuration
- Self-signed certificate generation
- Keystore creation
- Tomcat SSL connector configuration
- Custom domain support

## Building the Application

### Prerequisites
- Java 8 or higher
- Maven 3.6.0 or higher
- Tomcat 9.0 or higher
- PostgreSQL 12 or higher

### Build Steps

1. **Setup Database:**
```bash
cd /home/divakar-pt8008/Documents/Servlet/Task/ResourceManager
bash setup-db.sh
```

2. **Build Application:**
```bash
bash build.sh
```

This creates `target/ROOT.war`

3. **Create SSL Certificate:**
```bash
bash create-ssl-certificate.sh resourcemanager.local
```

4. **Deploy to Tomcat:**
```bash
bash deploy-to-tomcat.sh
```

## Running the Application

### Start Tomcat
```bash
$CATALINA_HOME/bin/startup.sh
```

### Access the Application
- HTTP: http://localhost:8080
- HTTPS: https://resourcemanager.local:8443

### Default Credentials
- Username: admin
- Password: admin

## API Usage

### Get Resource Details
```bash
curl -H "Cookie: JSESSIONID=<session-id>" \
     https://resourcemanager.local:8443/api/v1/resourcemanager/resource/1
```

Response:
```json
{
    "id": "res1",
    "name": "Database Server"
}
```

## Logs

Application logs are written to: `/tmp/resourcemanager.log`

Tomcat logs are located at: `$CATALINA_HOME/logs/`

### Log Format
```
[YYYY-MM-DD HH:MM:SS] LEVEL - Message
```

### Log Levels
- INFO: Normal operations
- ERROR: Error conditions
- DEBUG: Debug information

## Session Configuration

- Timeout: 5 minutes
- Storage: Server-side
- Security: HTTP-Only cookies enabled
- Protocol: HTTPS recommended

## Security Features

- Session validation via authentication filter
- HTTP-Only cookies
- Secure cookie flag enabled
- HTTPS/SSL support
- Password authentication
- SQL injection prevention via prepared statements

## Troubleshooting

### Database Connection Issues
- Check PostgreSQL is running
- Verify database credentials in DatabaseConnection.java
- Check that database "test" exists

### SSL Certificate Issues
- Verify keystore file permissions
- Check keystore password matches server.xml configuration
- Ensure domain is mapped in /etc/hosts

### Session Expiration
- Default timeout is 5 minutes
- Modify in LoginServlet.java SESSION_TIMEOUT constant
- Configure in web.xml session-config element

### API Not Working
- Check Authentication Filter allows /api paths
- Verify session is active
- Check resource ID exists in database
- Review logs in /tmp/resourcemanager.log

## File Locations

- Source Code: `/home/divakar-pt8008/Documents/Servlet/Task/ResourceManager/`
- WAR File: `target/ROOT.war`
- Logs: `/tmp/resourcemanager.log`
- Tomcat: `/usr/local/tomcat/` (default)
- Keystore: `/usr/local/tomcat/conf/resourcemanager-keystore.jks`

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Resources Table
```sql
CREATE TABLE resources (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Deployment Package

The deployment package should include:
1. Application source code
2. ROOT.war file
3. ResourceManager.jar (created by maven-shade-plugin)
4. Tomcat logs
5. server.xml with SSL configuration
6. SSL certificate keystore
7. This README.md file
