# ResourceManager Application - Deployment Package

## Quick Start

1. **View the project structure:**
   ```bash
   cd /home/divakar-pt8008/Documents/Servlet/Task/ResourceManager
   ls -la
   ```

2. **Run complete deployment:**
   ```bash
   ./complete-deployment.sh
   ```

3. **Quick start (if already deployed):**
   ```bash
   ./quickstart.sh
   ```

4. **Test the application:**
   ```bash
   ./test-application.sh
   ```

## Project Structure

```
ResourceManager/
├── src/
│   ├── main/
│   │   ├── java/com/resourcemanager/
│   │   │   ├── servlet/
│   │   │   │   ├── LoginServlet.java       (Login handler)
│   │   │   │   ├── LogoutServlet.java      (Logout handler)
│   │   │   │   └── ResourceServlet.java    (API endpoint)
│   │   │   ├── filter/
│   │   │   │   └── AuthenticationFilter.java (Session validation)
│   │   │   ├── dao/
│   │   │   │   ├── UserDAO.java            (User DB operations)
│   │   │   │   └── ResourceDAO.java        (Resource DB operations)
│   │   │   └── util/
│   │   │       ├── DatabaseConnection.java (DB connection)
│   │   │       └── Logger.java             (Application logging)
│   │   └── webapp/
│   │       ├── login.jsp                  (Login page)
│   │       ├── home.jsp                   (Home/dashboard page)
│   │       ├── index.jsp                  (Index redirect)
│   │       ├── css/style.css              (Styling)
│   │       └── WEB-INF/web.xml            (Web configuration)
├── target/
│   └── ROOT.war                           (Built application)
├── lib/                                    (Dependencies)
├── pom.xml                                (Maven configuration)
├── database-setup.sql                     (Database schema)
├── setup-db.sh                            (Database setup script)
├── build.sh                               (Build script)
├── create-ssl-certificate.sh              (SSL certificate generation)
├── deploy-to-tomcat.sh                    (Tomcat deployment)
├── complete-deployment.sh                 (Full deployment automation)
├── quickstart.sh                          (Quick startup)
├── test-application.sh                    (Application testing)
├── ssl-connector-config.xml               (Tomcat SSL config)
└── README.md                              (This file)
```

## Features

### Pages
- **Login Page** (`login.jsp`)
  - Username and password input
  - Form validation
  - Error message display
  - Responsive design

- **Home Page** (`home.jsp`)
  - Top navigation bar with branding
  - Left sidebar with navigation menu
  - User profile with avatar
  - Dropdown menu with logout
  - Dashboard with resource loading via AJAX
  - Responsive layout

### Servlets & APIs

1. **LoginServlet** (`/login`)
   - POST: Handle user login
   - GET: Redirect to login page
   - Creates session on success
   - Logs all login attempts

2. **LogoutServlet** (`/logout`)
   - Clears user session
   - Logs logout events
   - Redirects to login page

3. **ResourceServlet** (`/api/v1/resourcemanager/{resource-name}/{resource-id}`)
   - GET: Retrieve resource details (ID, name)
   - POST: Create resource
   - Returns JSON responses
   - Comprehensive error handling
   - Logging for all requests

### Authentication Filter
- Validates session on each request
- Redirects to login if session invalid/expired
- Allows public paths (login, CSS, JS)
- Enforces HTTPS security attributes

### Session Management
- 5-minute timeout (configurable)
- Session attributes: username, userId
- Automatic invalidation on logout
- HTTP-Only cookie support
- Secure flag enabled

### Logging
- Application logs to: `/tmp/resourcemanager.log`
- Tomcat logs: `$CATALINA_HOME/logs/`
- Logs include:
  - Login success/failure with timestamp
  - Logout events
  - API requests and responses
  - Exceptions with stack traces
  - Resource access

### Database Integration
- PostgreSQL connection
- User authentication
- Resource retrieval
- Prepared statements for security
- Connection pooling

### SSL/TLS Support
- Self-signed certificate generation
- Custom domain support (resourcemanager.local)
- Tomcat SSL connector configuration
- HTTPS port 8443

## Building the Application

### Prerequisites
```bash
# Java 8+
java -version

# Maven 3.6.0+
mvn -version

# PostgreSQL
psql --version

# Tomcat 9.0+
ls $TOMCAT_HOME/bin/
```

### Build Steps

1. **Setup Database:**
   ```bash
   psql -U divakar-pt8008 -d postgres -f database-setup.sql
   ```

2. **Build Project:**
   ```bash
   mvn clean package -DskipTests
   ```

3. **Generate SSL Certificate:**
   ```bash
   ./create-ssl-certificate.sh resourcemanager.local
   ```

4. **Deploy to Tomcat:**
   ```bash
   cp target/ROOT.war $TOMCAT_HOME/webapps/
   ```

5. **Configure Tomcat SSL:**
   - Add SSL connector to `$TOMCAT_HOME/conf/server.xml`
   - Copy content from `ssl-connector-config.xml`

6. **Update /etc/hosts:**
   ```bash
   echo "127.0.0.1 resourcemanager.local" | sudo tee -a /etc/hosts
   ```

## Running the Application

### Start Tomcat
```bash
$TOMCAT_HOME/bin/startup.sh
```

### Access the Application
- **HTTP:** http://localhost:8080
- **HTTPS:** https://resourcemanager.local:8443

### Default Credentials
- **Username:** admin
- **Password:** admin

## API Examples

### Login
```bash
curl -X POST https://resourcemanager.local:8443/login \
  -d "username=admin&password=admin" \
  -k -c cookies.txt
```

### Get Resource
```bash
curl https://resourcemanager.local:8443/api/v1/resourcemanager/resource/1 \
  -k -b cookies.txt
```

Response:
```json
{
  "id": "res1",
  "name": "Database Server"
}
```

### Logout
```bash
curl https://resourcemanager.local:8443/logout \
  -k -b cookies.txt
```

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

## Troubleshooting

### Database Connection Failed
```bash
# Check PostgreSQL is running
sudo systemctl status postgresql

# Verify credentials
psql -U divakar-pt8008 -d test

# Check database exists
psql -U divakar-pt8008 -d test -c "\dt"
```

### SSL Certificate Issues
```bash
# List certificates
keytool -list -v -keystore $TOMCAT_HOME/conf/resourcemanager-keystore.jks -storepass changeit

# Verify domain in /etc/hosts
cat /etc/hosts | grep resourcemanager.local
```

### Application Not Starting
```bash
# Check Tomcat logs
tail -f $TOMCAT_HOME/logs/catalina.out

# Check application logs
tail -f /tmp/resourcemanager.log

# Verify port 8080/8443 are available
lsof -i :8080
lsof -i :8443
```

### Session Expiration Issues
- Default timeout: 5 minutes
- Modify in `LoginServlet.java` line: `SESSION_TIMEOUT`
- Or in `web.xml` session-config section

## Files in Deployment Package

When you zip the source files, include:

1. **Source Code:**
   - All `.java` files in `src/`
   - All `.jsp` files in `src/main/webapp/`
   - `pom.xml`

2. **Build Artifacts:**
   - `target/ROOT.war` (built application)
   - `target/original-ROOT.war` (original WAR)

3. **Configuration:**
   - `src/main/webapp/WEB-INF/web.xml`
   - `ssl-connector-config.xml`

4. **Database:**
   - `database-setup.sql`

5. **Scripts:**
   - `build.sh`
   - `setup-db.sh`
   - `create-ssl-certificate.sh`
   - `deploy-to-tomcat.sh`
   - `complete-deployment.sh`
   - `quickstart.sh`
   - `test-application.sh`

6. **Logs & Certificates:**
   - `/tmp/resourcemanager.log`
   - `/usr/local/tomcat/logs/` (Tomcat logs)
   - `/usr/local/tomcat/conf/server.xml` (with SSL config)
   - `/usr/local/tomcat/conf/resourcemanager-keystore.jks` (certificate)

7. **Documentation:**
   - `README.md`
   - This file

## Contact & Support

For issues or questions about the ResourceManager application, refer to:
- Application logs: `/tmp/resourcemanager.log`
- Tomcat logs: `$TOMCAT_HOME/logs/catalina.out`
- Source code: Check specific servlet/filter classes

## License

This application is provided as-is for educational purposes.
