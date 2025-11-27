# Task Tracker

A lightweight Java + Maven web application for creating and managing simple tasks.
This project demonstrates a minimal layered web application (controller -> service -> DAO -> database) using Jakarta Servlets and plain JDBC. It is designed for learning, demos, or as a starting point for a small task management microservice.

---

## Highlights

- Simple REST-like HTTP endpoints implemented as servlets (no Spring).
- JDBC-based DAO layer with PostgreSQL as the reference database.
- Minimal dependencies (Jakarta Servlet API, org.json, PostgreSQL JDBC driver).
- Packaged as a WAR via Maven for deployment to any servlet container (Tomcat, Jetty, etc.).

## Tech stack

- Java 17+ (works with Java 21) — language and runtime.
- Maven — build and dependency management.
- Jakarta Servlet API — web layer (servlets annotated with `@WebServlet`).
- org.json — JSON parsing/generation used by controllers.
- PostgreSQL (example) + JDBC — persistence (the code expects the PostgreSQL driver by default).

Why each is used:
- Java: main application language.
- Maven: compile, package, run tests, produce WAR.
- Jakarta Servlets: lightweight web endpoints without a full framework.
- org.json: parse request bodies and build JSON responses.
- PostgreSQL/JDBC: simple relational persistence using standard JDBC APIs.

## Project overview and architecture

The application follows a small layered architecture:

- Controller (servlets) — handle HTTP requests, validate input, and call the service layer.
- Service — business logic and orchestration (uses DAO to interact with DB).
- DAO — database queries using JDBC and the `DBConnection` helper.
- Model — plain Java objects representing domain entities (e.g., `TaskModel`).

Flow example: HTTP POST /create -> `TaskController` -> `TaskService.createTask(...)` -> `TaskDAO.createTask(...)` -> DB

## Folder structure

```
pom.xml
src/
   main/
      java/
         com/tracker/
            controller/   # servlets / HTTP handlers
            service/      # business logic
            dao/          # JDBC DAOs
            model/        # domain models (TaskModel)
            util/         # DBConnection, Log
      resources/
         application.properties  # DB credentials and other configuration
      webapp/
         index.html
```

Key files:
- `src/main/java/com/tracker/controller/*` — servlet endpoints
- `src/main/java/com/tracker/service/TaskService.java` — service methods
- `src/main/java/com/tracker/dao/TaskDAO.java` — SQL queries
- `src/main/java/com/tracker/util/DBConnection.java` — reads `application.properties` and returns a JDBC Connection
- `src/main/resources/application.properties` — database connection config (db.url, db.username, db.password)

## Database schema (recommended)

The code expects a `tasks` table. Example SQL (PostgreSQL):

```sql
CREATE TABLE tasks (
   id SERIAL PRIMARY KEY,
   title VARCHAR(255) NOT NULL,
   description TEXT,
   status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
   created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);
```

Notes:
- `TaskDAO` reads and writes `id`, `title`, `description`, and `status`.
- `created_at` is optional for the current DAO implementation but useful for auditing.

## Configuration

Open `src/main/resources/application.properties` and provide your DB credentials. The project loads these keys:

```
db.url=jdbc:postgresql://localhost:5432/tasks_db
db.username=your_user
db.password=your_password
```

DB driver: `DBConnection` currently calls `Class.forName("org.postgresql.Driver")`, so include the PostgreSQL JDBC driver in `pom.xml` (it is typically present; if not, add it).

## Endpoints (routes)

Below are the HTTP endpoints implemented in the project (detected from controller annotations):

- GET /getalltask
   - Description: Return a JSON array of all tasks. Optional query param `status` to filter by status.
   - Example: GET /getalltask
   - Example filtered: GET /getalltask?status=COMPLETED

- GET /getbyid/{id}
   - Description: Return a single task by ID (path param).
   - Example: GET /getbyid/3

- POST /create
   - Description: Create a new task. Accepts JSON body with `title` (required) and `description` (optional).
   - Example request body:
      ```json
      { "title": "Buy groceries", "description": "Milk, eggs" }
      ```

- PUT /task/update/{id}
   - Description: Update the task's `status` by ID. Accepts JSON body with `status` (e.g., `COMPLETED`, `PENDING`).
   - Example: PUT /task/update/3 with body `{ "status": "COMPLETED" }`

- DELETE /task/delete/{id}
   - Description: Delete a task by ID.
   - Example: DELETE /task/delete/3

- GET /sampe
   - Description: Sample/debug endpoint (prints a message) — note: path is `/sampe` (typo preserved from code).

Example curl (create):

```bash
curl -X POST -H "Content-Type: application/json" \
   -d '{"title":"Test","description":"desc"}' \
   http://localhost:8080/<context-root>/create
```

Example curl (get all):

```bash
curl http://localhost:8080/<context-root>/getalltask
```

Replace `<context-root>` with the deployed webapp context (for example, `task-tracker-0.0.1-SNAPSHOT`).

## Build and package

Requirements:
- JDK 17+ (JDK 21 tested)
- Maven 3.x

Build:

```bash
mvn clean package
```

The produced WAR will be under `target/`.

## Run / Deploy

1) Deploy WAR to a servlet container such as Tomcat or Jetty.
    - Copy `target/*.war` to the container's deployment folder and start the server.

2) Run with Maven plugin (if configured in `pom.xml`):

```bash
mvn jetty:run
```

If not configured, deploy to a container manually.

## Tests

Run unit tests (if present):

```bash
mvn test
```

## Development notes and potential improvements

- Add input validation and better error handling in controllers.
- Use a connection pool (HikariCP or similar) rather than calling DriverManager for each request.
- Add prepared schema migrations (Flyway or Liquibase) and test data.
- Consider migrating to a lightweight framework (Spring Boot) for easier wiring and auto-configuration.
- Improve JSON handling and response codes (use consistent JSON response objects and HTTP status codes).

## Contribution

1. Fork the repo
2. Create a branch per feature/bugfix
3. Add tests for new behavior
4. Open a PR with a clear description

## License

Add a license file (e.g. MIT) to the repo and state it here. If you want, I can add a default MIT license.

## Contact

Include your contact information or project owner details here.

---

If you'd like, I can also:

- Add a sample `src/main/resources/application.properties.example` with dummy values.
- Add a SQL migration file `db/migration/V1__init.sql` containing the schema above.
- Create a small GitHub Actions workflow to build and run tests on each push.

Tell me which of these you'd like next and I'll add it.
