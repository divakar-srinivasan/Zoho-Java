# Backend Project - Task Tracker Application

## 📋 Project Overview

This is a **Java-based Backend Application** built with servlet technology and PostgreSQL database. The application provides authentication services (signup/login) and message management features with a robust architecture following MVC (Model-View-Controller) design pattern.

### Quick Summary
- **Type**: Java Web Application (WAR)
- **Framework**: Jakarta Servlets (Servlet 6.0)
- **Database**: PostgreSQL
- **Build Tool**: Maven
- **Java Version**: Java 21
- **Status**: In Development (v0.0.1)

---

## 🎯 Project Description

The Task Tracker Backend is designed to handle:
- **User Authentication**: Signup and Login with secure password hashing
- **Message Management**: Insert, publish, and track messages
- **Partition Management**: Handle message partitions for scalability
- **Security**: JWT/Filter-based authentication and BCrypt password encryption
- **Logging**: Comprehensive logging using SLF4J with Logback
- **Connection Pooling**: HikariCP for efficient database connection management

---

## 🏗️ Folder Structure

```
backend/
├── pom.xml                          # Maven configuration file
├── README.md                        # Project documentation
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/backend/
│   │   │       ├── controller/          # HTTP Request Handlers
│   │   │       │   ├── SignupController.java       # User signup endpoint
│   │   │       │   ├── LoginController.java        # User login endpoint
│   │   │       │   ├── InsertMessage.java          # Message insertion endpoint
│   │   │       │   ├── PublishMessage.java         # Message publishing endpoint
│   │   │       │   └── test.java                   # Testing controller
│   │   │       ├── dao/                 # Data Access Objects
│   │   │       │   ├── LoginDao.java                # User authentication DB operations
│   │   │       │   ├── MessageDao.java              # Message DB operations
│   │   │       │   ├── InsertSubTable.java          # Subscription table operations
│   │   │       │   └── PartitionDao.java            # Partition management
│   │   │       ├── filter/              # Request Filters
│   │   │       │   └── AuthFilter.java              # Authentication filter for secured endpoints
│   │   │       ├── listener/            # Application Lifecycle Listeners
│   │   │       │   └── BackendListener.java         # Application startup/shutdown handler
│   │   │       ├── model/               # Data Models
│   │   │       │   └── MessageModel.java            # Message data model
│   │   │       ├── service/             # Business Logic (Service Layer)
│   │   │       ├── util/                # Utility Classes
│   │   │       │   ├── DbConnection.java            # Database connection pool (HikariCP)
│   │   │       │   └── Log.java                     # Logging utility
│   │   │       └── worker/              # Background Workers
│   │   │           └── MessageWorker.java           # Async message processing
│   │   ├── resources/
│   │   │   └── application.properties   # Application configuration
│   │   └── webapp/
│   │       └── index.html               # Frontend entry point
│   └── test/
│       └── java/                        # Unit tests (placeholder)
└── target/                              # Compiled output (Maven build artifacts)
    ├── backend-0.0.1/                   # Deployable WAR structure
    ├── classes/                         # Compiled Java classes
    └── generated-sources/               # Generated code
```

---

## 🗄️ Database Architecture

### Database: PostgreSQL

The application connects to a PostgreSQL database with the following main tables:

#### **Users Table**
```sql
CREATE TABLE users (
    user_id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,  -- BCrypt hashed password
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### **Messages Table** (Inferred from code)
```sql
CREATE TABLE messages (
    message_id SERIAL PRIMARY KEY,
    user_id VARCHAR(255) FOREIGN KEY,
    content TEXT,
    partition_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### **Partitions Table** (Inferred from code)
```sql
CREATE TABLE partitions (
    partition_id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### **Subscriptions Table** (Inferred from code)
```sql
CREATE TABLE subscriptions (
    subscription_id SERIAL PRIMARY KEY,
    user_id VARCHAR(255) FOREIGN KEY,
    partition_id INT FOREIGN KEY,
    subscribed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Connection Details**:
- **Host**: localhost
- **Port**: 5432
- **Database**: tasktracker
- **Username**: divakar-pt8008
- **Password**: (See application.properties)

---

## 🛠️ Tech Stack & Dependencies

### Core Technologies
| Component | Version | Purpose |
|-----------|---------|---------|
| **Java** | 21 | Programming language |
| **Maven** | Latest | Build & dependency management |
| **Jakarta Servlets** | 6.0.0 | Web application framework |
| **Jakarta JSP** | 3.1.0 | Server-side template engine |
| **PostgreSQL** | 42.6.0 | Relational database driver |

### Key Libraries & Their Uses

| Library | Version | Purpose |
|---------|---------|---------|
| **HikariCP** | 5.0.1 | Connection pooling for optimal DB performance |
| **BCrypt (jbcrypt)** | 0.4 | Secure password hashing & verification |
| **SLF4J** | 2.0.7 | Logging facade for flexible logging |
| **Logback** | 1.4.11 | SLF4J implementation for structured logging |
| **JSON-org** | 20230227 | JSON parsing and object serialization |
| **JSTL** | 3.0.0 | JSP Standard Tag Library for templating |

### Build Tools
- **Maven Compiler Plugin** (3.11.0) - Compiles source code for Java 21
- **Maven WAR Plugin** (3.3.2) - Packages application as WAR file

---

## 🚀 Project Setup

### Prerequisites
Ensure you have the following installed:

1. **Java Development Kit (JDK) 21+**
   ```bash
   java -version
   ```
   
2. **Maven 3.8+**
   ```bash
   mvn -version
   ```

3. **PostgreSQL 12+**
   ```bash
   psql --version
   ```

4. **Tomcat 10+ or similar Servlet Container** (for deployment)

### Installation Steps

#### 1. Clone or Extract the Project
```bash
cd /home/divakar-pt8008/Documents/Java/backend
```

#### 2. Configure Database Connection
Edit `src/main/resources/application.properties`:
```properties
# PostgreSQL Connection Settings
db.url = jdbc:postgresql://localhost:5432/tasktracker
db.username = your_postgres_username
db.password = your_postgres_password

# Connection Pool Settings (Optional)
db.pool.size = 10
db.pool.minimumIdle = 2
db.pool.connectionTimeoutMs = 30000
db.pool.leakDetectionThresholdMs = 60000
```

#### 3. Create PostgreSQL Database
```bash
psql -U postgres

# In PostgreSQL console:
CREATE DATABASE tasktracker;
\c tasktracker

-- Create tables (use the schemas provided above)
CREATE TABLE users (
    user_id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE partitions (
    partition_id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE messages (
    message_id SERIAL PRIMARY KEY,
    user_id VARCHAR(255) REFERENCES users(user_id),
    partition_id INT REFERENCES partitions(partition_id),
    content TEXT
);

CREATE TABLE subscriptions (
    subscription_id SERIAL PRIMARY KEY,
    user_id VARCHAR(255) REFERENCES users(user_id),
    partition_id INT REFERENCES partitions(partition_id)
);
```

#### 4. Build the Project
```bash
cd /home/divakar-pt8008/Documents/Java/backend
mvn clean install
```

This generates the WAR file: `target/backend-0.0.1.war`

---

## 📦 Build & Execution

### Maven Build Commands

#### Full Build
```bash
mvn clean install
```

#### Compile Only
```bash
mvn compile
```

#### Run Tests
```bash
mvn test
```

#### Skip Tests
```bash
mvn install -DskipTests
```

#### Create WAR Package
```bash
mvn package
```

### Deployment

#### Option 1: Using Tomcat (Recommended)
```bash
# Copy WAR to Tomcat
cp target/backend-0.0.1.war /path/to/tomcat/webapps/

# Start Tomcat
/path/to/tomcat/bin/startup.sh

# Application runs at: http://localhost:8080/backend-0.0.1
```

#### Option 2: Using Maven Tomcat Plugin
```bash
mvn tomcat7:deploy
```

---

## 📡 API Endpoints

### Authentication Endpoints

#### Signup
```
POST /signup
Content-Type: application/json

Request Body:
{
    "user_id": "user123",
    "username": "john_doe",
    "password": "securePassword123"
}

Response (Success - 200):
{
    "Status": "SUCCESS",
    "username": "john_doe"
}

Response (Conflict - 409):
{
    "error": "Unable to register user"
}
```

#### Login
```
POST /login
Content-Type: application/json

Request Body:
{
    "user_id": "user123",
    "password": "securePassword123"
}

Response (Success - 200):
{
    "Status": "SUCCESS",
    "auth_token": "jwt_token_here"
}

Response (Unauthorized - 401):
{
    "error": "Invalid credentials"
}
```

### Message Endpoints

#### Insert Message
```
POST /message/insert
Authorization: Bearer <auth_token>
Content-Type: application/json

Request Body:
{
    "partition_id": 1,
    "content": "Message content here"
}
```

#### Publish Message
```
POST /message/publish
Authorization: Bearer <auth_token>
Content-Type: application/json
```

---

## 🔒 Security Features

1. **Password Hashing**: BCrypt with salt factor 12 for secure password storage
2. **Authentication Filter**: `AuthFilter.java` validates requests on secured endpoints
3. **Connection Pool**: HikariCP with leak detection for secure connection management
4. **Prepared Statements**: SQL injection prevention through parameterized queries
5. **JSON Validation**: Strict JSON parsing with error handling

---

## 📝 Application Configuration

The `application.properties` file contains key configurations:

```properties
# Database
db.url=jdbc:postgresql://localhost:5432/tasktracker
db.username=divakar-pt8008
db.password=Divakar@2005

# Connection Pool (Optional)
db.pool.size=10
db.pool.minimumIdle=2
db.pool.connectionTimeoutMs=30000
db.pool.leakDetectionThresholdMs=60000
```

---

## 🎯 Architecture Patterns

### MVC Pattern
- **Model**: `MessageModel.java` - Data structures
- **View**: `index.html` - Frontend interface
- **Controller**: `*Controller.java` - Request handling
- **DAO**: Database operations layer

### Key Design Patterns Used
1. **DAO Pattern**: Data Access Objects for database operations
2. **Connection Pool Pattern**: HikariCP for efficient resource management
3. **Filter Pattern**: Authentication filter for cross-cutting concerns
4. **Listener Pattern**: Application lifecycle management

---

## 🧪 Testing

Currently, placeholder test files exist in `src/test/java/`. To run tests:

```bash
mvn test
```

To add tests, create test classes following JUnit 5 conventions.

---

## 📊 Logging

The application uses SLF4J with Logback for structured logging:

```java
// Usage in code
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

private static final Logger logger = LoggerFactory.getLogger(ClassName.class);
logger.info("Message info");
logger.error("Error occurred", exception);
```

Logs can be configured in `logback-spring.xml` (if present) or defaults are used.

---

## 🚨 Troubleshooting

### Database Connection Issues
- Verify PostgreSQL is running: `psql -U postgres -d tasktracker`
- Check credentials in `application.properties`
- Ensure database exists and tables are created

### Build Errors
- Clear Maven cache: `mvn clean`
- Verify Java version: `java -version` (should be 21+)
- Check Maven version: `mvn -version`

### Deployment Issues
- Ensure Tomcat version is 10+ (supports Jakarta Servlets)
- Check WAR file is generated: `ls -la target/backend-*.war`
- Verify port 8080 is not in use

---

## 📚 Project Roadmap

- [ ] Implement Service Layer for business logic
- [ ] Add comprehensive unit tests
- [ ] Implement JWT authentication
- [ ] Add Swagger/OpenAPI documentation
- [ ] Implement async message processing
- [ ] Add API rate limiting
- [ ] Database migration scripts (Flyway/Liquibase)
- [ ] Docker containerization

---

## 👥 Contributing

1. Follow the existing code structure and naming conventions
2. Ensure all code is properly logged
3. Write unit tests for new features
4. Use prepared statements for all DB queries (prevent SQL injection)

---

## 📄 License

This project is private and confidential.

---

## 📞 Support

For issues and questions, refer to the project documentation or contact the development team.

---

## 📅 Version History

| Version | Date | Changes |
|---------|------|---------|
| 0.0.1 | Nov 2024 | Initial project setup with auth endpoints |

---

**Last Updated**: November 27, 2025
