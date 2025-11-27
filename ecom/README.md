# ecom

Comprehensive README for the ecom sample web application.

## Project summary

ecom is a Java web application (WAR) that demonstrates a simple e‑commerce backend structure. It uses Jakarta Servlet/JSP for the web layer, a DAO layer for database access, HikariCP for connection pooling, and PostgreSQL as the primary data store.

This README gives a quick review, project overview, folder structure, database configuration, tech stack and usage, plus step‑by‑step setup and execution instructions.

## Quick review
- Purpose: sample e‑commerce backend showcasing controllers, DAOs, worker threads, and DB connectivity.
- Packaging: WAR (see `pom.xml` -> `<packaging>war</packaging>`). Build produces `target/ecom-0.0.1.war`.
- Runtime: deploy the WAR into a Jakarta Servlet container (Tomcat 10+, Jetty, WildFly) or run with an embedded container if added.

## Overview / architecture

- Presentation: JSPs under `src/main/webapp` (simple index page exists).
- Controllers: Servlet controllers in `com.ecom.controller` handle HTTP requests.
- Persistence: DAO classes in `com.ecom.dao` interact with the DB using JDBC. Connection pooling is handled by HikariCP via `com.ecom.util.Db`.
- Models: POJOs under `com.ecom.model` (e.g., `OrdersModel`).
- Background workers/listeners: `com.ecom.workers` (e.g., `MainThread`, `SubThread`) and `com.ecom.listener.BackendListener` for lifecycle events.
- Utilities: `com.ecom.util` contains `Db.java` (Hikari) and `Log.java` wrappers.

## Folder structure

Top-level (relevant parts):

```
pom.xml
src/
  main/
    java/
      com/ecom/
        controller/    # Servlets and controllers
        dao/           # Data access objects
        listener/      # App/listener classes
        model/         # Domain models
        util/          # Db, logging, helpers
        workers/       # Background worker threads
    resources/
      application.properties  # DB and runtime properties (classpath)
    webapp/
      index.html
test/
target/
```

Note: `target/` contains the build output (exploded WAR or packaged WAR under `target/`).

## Database

- The application expects a PostgreSQL database by default. `com.ecom.util.Db` loads `application.properties` from the classpath and expects the following keys:

- `db.url` (JDBC URL, e.g. `jdbc:postgresql://localhost:5432/ecom`)
- `db.username`
- `db.password`

Example values found in the repository's `src/main/resources/application.properties` (for development only):

```
db.url = jdbc:postgresql://localhost:5432/ecom
db.username = YOUR_DB_USER
db.password = YOUR_DB_PASSWORD
```

Important: Do NOT commit production passwords into source control. Use environment-specific configuration (external properties, CI secrets, or container secrets) for real deployments.

Postgres quick setup (local):

```bash
# as postgres superuser
psql -U postgres -c "CREATE DATABASE ecom;"
psql -U postgres -c "CREATE USER ecom_user WITH PASSWORD 'change_me';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE ecom TO ecom_user;"
```

Then update `src/main/resources/application.properties` with the same credentials for local testing.

How the app connects:

- `Db.java` uses HikariCP and explicitly loads the PostgreSQL JDBC driver (`org.postgresql.Driver`). Hikari settings are defined in code (max pool size, timeouts).

## Tech stack

- Java 21 (source/target)
- Maven for build and dependency management
- Jakarta Servlet API / JSP for web layer
- PostgreSQL (JDBC driver)
- HikariCP connection pool
- SLF4J + Logback for logging
- jBCrypt for password hashing

Why these are used:
- Java 21: modern language features and performance.
- Maven: standard Java build tool.
- Jakarta Servlet/JSP: lightweight server-side UI for a classic Java webapp.
- PostgreSQL: reliable relational DB for transactional data.
- HikariCP: fast, production-ready connection pool.
- SLF4J/Logback: flexible logging.

## Project setup (local development)

Prerequisites
- Java 21 JDK installed and JAVA_HOME set.
- Maven 3.8+ installed.
- PostgreSQL running locally or accessible remotely.

Steps

1. Clone the repository or open the project in your IDE.
2. Create the PostgreSQL database and user (see Database section).
3. Update `src/main/resources/application.properties` with your DB URL, username and password.
   - Alternatively, place an `application.properties` on the runtime classpath that contains those keys.
4. Build the project using Maven:

```bash
mvn clean package
```

This produces `target/ecom-0.0.1.war`.

## Execution / Deployment

Option A — Deploy to a servlet container (recommended):

1. Copy `target/ecom-0.0.1.war` to your Tomcat `webapps/` (or other Jakarta container) and start the container.
2. Access the app at http://localhost:8080/ecom/ (container- and context-dependent).

Option B — Run inside a container (Docker) by deploying the WAR to a Tomcat image:

```bash
# example: copy the built WAR into a running Tomcat container or mount it
docker run --rm -p 8080:8080 -v $(pwd)/target/ecom-0.0.1.war:/usr/local/tomcat/webapps/ecom.war tomcat:10.1-jdk17
```

Note: Ensure Tomcat / container JVM version is compatible with compiled bytecode (Java 21). You may need to build/run with a matching image or add an embedded server to the project.

## Configuration tips and security
- Do not store production credentials in `application.properties` within the repo. Use environment variables, externalized config, or secret managers.
- To externalize runtime properties, place an `application.properties` on the container classpath or configure your servlet container to provide system properties.
- Consider adding a small wrapper to read DB credentials from environment variables and fallback to classpath properties (if you plan to harden this project).

## Developer notes
- The connection pool is created in a static initializer in `Db.java`. If you need different pool settings per environment, refactor to allow external configuration.
- Background workers (`MainThread`, `SubThread`) are present — check `listener/BackendListener.java` for lifecycle wiring (start/stop behavior).

## Next steps / improvements (suggested)
- Add a `.env.example` or `config` template and document how to manage secrets.
- Add CI build/test steps and automated checks.
- Add integration tests for DAOs using a test Postgres container (Testcontainers).
- Provide an example `docker-compose.yml` to run the app with Postgres locally.

## Where to look in the code
- Controllers: `src/main/java/com/ecom/controller`
- DAOs: `src/main/java/com/ecom/dao`
- DB helper: `src/main/java/com/ecom/util/Db.java`
- Properties: `src/main/resources/application.properties`

---

If you'd like, I can also:

- Add a `.env.example` and change `Db.java` to prefer environment variables.
- Add a `docker-compose.yml` to run Postgres + Tomcat for local development.
- Add a small `CONTRIBUTING.md` and a license file.

Tell me which of those extras you'd like and I will add them next.
